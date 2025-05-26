package com.ssafy.yamyam.service;

import com.ssafy.yamyam.dao.DietDao;
import com.ssafy.yamyam.dto.DailyNutrition;
import com.ssafy.yamyam.dto.Diet;
import com.ssafy.yamyam.dto.User;
import com.ssafy.yamyam.util.OpenAIUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class GPTServiceImpl implements GPTService {

    private final DietDao dietDao;
    private final NutritionService nutritionService;
    private final OpenAIUtil openAIUtil;

    @Autowired
    public GPTServiceImpl(DietDao dietDao,
                          NutritionService nutritionService,
                          OpenAIUtil openAIUtil) {
        this.dietDao = dietDao;
        this.nutritionService = nutritionService;
        this.openAIUtil = openAIUtil;
    }

    @Override
    public String getFeedback(String userId, LocalDate date) {
        List<Diet> meals = dietDao.selectByUserIdAndDate(userId, date);
        if (meals == null || meals.isEmpty()) {
            return "해당 날짜의 식단 정보가 없습니다.";
        }

        DailyNutrition sum = calculateTotalNutrition(meals);

        DailyNutrition target = nutritionService.calculateNutritionTargets(userId);

        String prompt = buildPrompt(userId, date, sum, target, meals);
        return openAIUtil.callChatGPT(prompt);
    }

    private DailyNutrition calculateTotalNutrition(List<Diet> meals) {
        double kcal = 0, carb = 0, protein = 0, fat = 0;
        double fiber = 0, sugar = 0, transfat = 0;

        for (Diet d : meals) {
            kcal += d.getKcal();
            carb += d.getCarbo();
            protein += d.getProtein();
            fat += d.getFat();
            fiber += d.getFiber();
            sugar += d.getSugar();
            transfat += d.getTransfat();
        }

        int count = meals.size() == 0 ? 1 : meals.size();

        return new DailyNutrition(
                kcal / count,
                carb / count,
                protein / count,
                fat / count,
                fiber / count,
                sugar / count,
                transfat / count,
                0  // bmr은 GPT에서 안 쓰므로 0으로 대체
        );
    }


    private String buildPrompt(String userId, LocalDate date, DailyNutrition sum,
                               DailyNutrition target, List<Diet> meals) {

        // 사용자 정보 가져오기
        User user = nutritionService.getUserInfo(userId);  // 반드시 NutritionServiceImpl 에 이런 메서드 있어야 함
        String goal = user.getSpecialNote();                // 체중감량 / 유지 / 근육량 증가
        String activityLevel = user.getActivityLevel();     // 예: "활동 적음", "보통", "많음"

        StringBuilder sb = new StringBuilder();
        sb.append("다음은 ").append(userId)
                .append(" 사용자의 ").append(date).append(" 식단 기록입니다. ")
                .append("사용자는 현재 '").append(goal).append("'을 목표로 하고 있으며, 활동량은 '")
                .append(activityLevel).append("' 수준입니다.\n");

        sb.append("\n[식단 내역]\n");
        for (Diet d : meals) {
            sb.append("- ").append(d.getFoodName()).append(": ")
                    .append(d.getKcal()).append("kcal, ")
                    .append(d.getCarbo()).append("g 탄수화물, ")
                    .append(d.getProtein()).append("g 단백질, ")
                    .append(d.getFat()).append("g 지방\n");
        }

        sb.append("\n[총 섭취량]\n")
                .append("칼로리: ").append(sum.getKcal()).append("kcal\n")
                .append("탄수화물: ").append(sum.getCarb()).append("g\n")
                .append("단백질: ").append(sum.getProtein()).append("g\n")
                .append("지방: ").append(sum.getFat()).append("g\n")
                .append("식이섬유: ").append(sum.getFiber()).append("g\n")
                .append("당류: ").append(sum.getSugar()).append("g\n")
                .append("트랜스지방: ").append(sum.getTransfat()).append("g\n");

        sb.append("\n[권장 섭취량 (해당 사용자 기준)]\n")
                .append("칼로리: ").append(target.getKcal()).append("kcal\n")
                .append("탄수화물: ").append(target.getCarb()).append("g\n")
                .append("단백질: ").append(target.getProtein()).append("g\n")
                .append("지방: ").append(target.getFat()).append("g\n")
                .append("식이섬유: ").append(target.getFiber()).append("g\n")
                .append("당류: ").append(target.getSugar()).append("g\n")
                .append("트랜스지방: ").append(target.getTransfat()).append("g\n");

        sb.append("\n위 데이터를 바탕으로 다음 사항을 반영하여 전문가 스타일로 피드백을 해주세요:\n")
                .append("1. 체중감량 / 근육량 증가 등 사용자의 목표에 적합한지 평가해주세요.\n")
                .append("2. 영양성분의 과잉 또는 부족 여부를 분석해주세요.\n")
                .append("3. 과잉/부족한 항목에 대해 구체적으로 어떤 음식을 줄이거나 추가하면 좋을지 알려주세요.\n")
                .append("4. 부족한 영양소를 보충할 수 있는 식품 예시를 제시하고, 그 식품의 쿠팡 구매 링크도 함께 제시해주세요. (단, 실제 링크는 예시 형식으로라도 보여주세요)\n")
                .append("5. ‘트레이너’나 ‘영양사’처럼 친절하고 설득력 있는 말투로 작성해주세요. 단순한 수치 나열이 아닌 실질적 조언이 되도록 해주세요.\n")
                .append("6. 내가 먹은 음식 중 바꾸면 좋은 음식이 있다면 어떤 걸 대체해서 바꿔보면 좋을지 구체적으로 제안해주세요.\n")
                .append("7. 쿠팡 링크는 반드시 'https://www.coupang.com/...' 형태의 전체 URL로 출력해주세요. 'coupang.com' 식의 도메인만 주지 마세요.\n" +
                        "8. 특정 카테고리 링크(예: /categories/xxxxx)는 사용하지 마세요.  \n" +
                        "항상 https://www.coupang.com/np/search?q=식품명 형식의 검색 링크만 추천해주세요\n" +
                        "그리고 넘겨받은 사용자의 정보(신체정보 및 활동량, 목표, 권장섭취량) 와 일일섭취량을 기반으로 식단의 점수를 매겨주세요. ");
        return sb.toString();
    }

}
