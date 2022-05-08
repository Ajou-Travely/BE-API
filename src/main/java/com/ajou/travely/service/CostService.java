package com.ajou.travely.service;

import com.ajou.travely.controller.cost.dto.CostCreateResponseDto;
import com.ajou.travely.controller.cost.dto.CostResponseDto;
import com.ajou.travely.controller.cost.dto.UserCostResponseDto;
import com.ajou.travely.controller.user.dto.SimpleUserInfoDto;
import com.ajou.travely.domain.Cost;
import com.ajou.travely.domain.Travel;
import com.ajou.travely.domain.UserCost;
import com.ajou.travely.domain.user.User;
import com.ajou.travely.repository.CostRepository;
import com.ajou.travely.repository.TravelRepository;
import com.ajou.travely.repository.UserCostRepository;
import com.ajou.travely.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CostService {
    private final CostRepository costRepository;

    private final UserRepository userRepository;

    private final UserCostRepository userCostRepository;

    private final TravelRepository travelRepository;

    @Transactional
    public CostCreateResponseDto createCost(Long totalAmount, Long travelId, String title, String content, Boolean isEquallyDivided, Map<Long, Long> amountsPerUser, Long payerId) {
        // 여행 객체 생성
        Travel travel = travelRepository.findById(travelId)
                .orElseThrow(() -> new RuntimeException("여행 없음 ㅋㅋ"));
        // 결제자 객체 생성
        User payer = userRepository.findById(payerId)
                .orElseThrow(() -> new RuntimeException("결제자 없음 ㅋㅋ"));
        // 지출 객체 생성
        Cost cost = Cost.builder()
                .totalAmount(totalAmount)
                .content(content)
                .title(title)
                .travel(travel)
                .payerId(payerId)
                .isEquallyDivided(isEquallyDivided)
                .build();
        // 사용자_지출 객체 생성
        for (Long userId : amountsPerUser.keySet()) {
            UserCost userCost = UserCost.builder()
                    .cost(cost)
                    .user(userRepository.findById(userId)
                            .orElseThrow(() -> new RuntimeException("유저 없음 ㅋㅋ")))
                    .amount(amountsPerUser.get(userId))
                    .build();
            userCostRepository.save(userCost);
            cost.addUserCost(userCost);
        }
        costRepository.save(cost);

        return new CostCreateResponseDto(cost, payer);
    }

    @Transactional(readOnly = true)
    public CostResponseDto getCostById(Long costId) {
        Optional<Cost> costFoundById = Optional.ofNullable(costRepository.getCostById(costId)
                .orElseThrow(() -> new RuntimeException("해당 지출을 찾을 수 없습니다.")));
        Cost cost = costFoundById.get();
        List<UserCostResponseDto> userCostResponseDtos = new ArrayList<>();
        cost.getUserCosts().forEach(userCost -> {
            userCostResponseDtos.add(new UserCostResponseDto(
                    userCost.getId(),
                    userCost.getAmount(),
                    new SimpleUserInfoDto(
                            userCost.getUser().getId(),
                            userCost.getUser().getName()
                    ),
                    userCost.getIsRequested()
            ));
        });
        CostResponseDto costResponseDto = new CostResponseDto(
                cost.getId(),
                cost.getTotalAmount(),
                cost.getContent(),
                cost.getTitle(),
                cost.getIsEquallyDivided(),
                userCostResponseDtos,
                cost.getPayerId()
        );

        return costResponseDto;
    }
}
