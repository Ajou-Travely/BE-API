package com.ajou.travely.service;

import com.ajou.travely.controller.cost.dto.CostResponseDto;
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
    public CostResponseDto createCost(Long totalAmount, Long travelId, String title, String content, Boolean isEquallyDivided, Map<Long, Long> amountsPerUser, Long payerId) {
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

        return new CostResponseDto(cost, payer);
    }
}
