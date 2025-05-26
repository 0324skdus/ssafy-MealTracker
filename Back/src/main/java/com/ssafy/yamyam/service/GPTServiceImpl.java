package com.ssafy.yamyam.service;

import com.ssafy.yamyam.dao.DietDao;
import com.ssafy.yamyam.dto.DailyNutrition;
import com.ssafy.yamyam.dto.Diet;
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

        // ✅ 총 섭취 영양소: 너의 NutritionServiceImpl 기준대로 호출
        DailyNutrition sum = calculateTotalNutrition(meals);

        // ✅ 권장 섭취 영양소
        DailyNutrition target = nutritionService.calculateNutritionTargets(userId);

        String prompt = buildPrompt(userId, date, sum, target, meals);
        return openAIUtil.callChatGPT(prompt);
    }

    // 너의 NutritionServiceImpl에는 총합 계산용 별도 메서드가 없어서 여기서 임시로 계산
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

        return new DailyNutrition(
                Math.round(kcal),
                Math.round(carb),
                Math.round(protein),
                Math.round(fat),
                Math.round(fiber),
                Math.round(sugar),
                Math.round(transfat)
        );
    }

    private String buildPrompt(String userId, LocalDate date, DailyNutrition sum,
                               DailyNutrition target, List<Diet> meals) {
        StringBuilder sb = new StringBuilder();
        sb.append(userId).append(" 사용자의 ").append(date).append(" 식단은 다음과 같습니다:\n");
        for (Diet d : meals) {
            sb.append("- ").append(d.getFoodName()).append(": ")
                    .append(d.getKcal()).append("kcal, ")
                    .append(d.getCarbo()).append("g 탄수화물, ")
                    .append(d.getProtein()).append("g 단백질, ")
                    .append(d.getFat()).append("g 지방\n");
        }

        sb.append("\n[실제 섭취량]\n")
                .append("칼로리: ").append(sum.getKcal()).append("kcal\n")
                .append("탄수화물: ").append(sum.getCarb()).append("g\n")
                .append("단백질: ").append(sum.getProtein()).append("g\n")
                .append("지방: ").append(sum.getFat()).append("g\n")
                .append("식이섬유: ").append(sum.getFiber()).append("g\n")
                .append("당류: ").append(sum.getSugar()).append("g\n")
                .append("트랜스지방: ").append(sum.getTransfat()).append("g\n");

        sb.append("\n[권장 섭취량]\n")
                .append("칼로리: ").append(target.getKcal()).append("kcal\n")
                .append("탄수화물: ").append(target.getCarb()).append("g\n")
                .append("단백질: ").append(target.getProtein()).append("g\n")
                .append("지방: ").append(target.getFat()).append("g\n")
                .append("식이섬유: ").append(target.getFiber()).append("g\n")
                .append("당류: ").append(target.getSugar()).append("g\n")
                .append("트랜스지방: ").append(target.getTransfat()).append("g\n");

        sb.append("\n위 내용을 바탕으로 이 식단이 건강한지, 어떤 개선이 필요한지 전문가 관점에서 상세 피드백을 작성해주세요. ");

        return sb.toString();
    }
}
