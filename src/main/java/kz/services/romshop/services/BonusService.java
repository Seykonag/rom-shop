package kz.services.romshop.services;

import jakarta.transaction.Transactional;
import kz.services.romshop.models.BonusScore;
import kz.services.romshop.models.User;
import kz.services.romshop.repositories.BonusRepository;
import kz.services.romshop.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class BonusService {
    private final UserRepository userRepository;
    private final BonusRepository bonusRepository;

    @Transactional
    public BonusScore createBonusScore(User user) {
        BonusScore bonusScore = new BonusScore();
        bonusScore.setId(user.getId());
        bonusScore.setUser(user);
        bonusScore.setSum(new BigDecimal(0));
        user.setBonusScore(bonusScore);
        userRepository.save(user);
        return bonusRepository.save(bonusScore);
    }
}
