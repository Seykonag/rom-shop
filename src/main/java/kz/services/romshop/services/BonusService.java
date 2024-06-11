package kz.services.romshop.services;

import jakarta.transaction.Transactional;
import kz.services.romshop.models.BonusScore;
import kz.services.romshop.models.Order;
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
    private final BonusRepository repository;

    @Transactional
    public BonusScore createBonusScore(User user) {
        BonusScore bonusScore = new BonusScore();
        bonusScore.setId(user.getId()); // Присваиваем id BonusScore таким же, как у пользователя
        bonusScore.setSum(BigDecimal.ZERO);
        bonusScore.setUser(user);// Можно использовать константу BigDecimal.ZERO

        // Сохраняем BonusScore перед сохранением User, чтобы избежать проблем с целостностью данных
        BonusScore savedBonusScore = repository.save(bonusScore);

        // Присваиваем сохраненный BonusScore пользователю
        user.setBonusScore(savedBonusScore);
        userRepository.save(user);

        return savedBonusScore;
    }


    public void additionBonus(User user, BigDecimal sum) {
        BonusScore bonusScore = repository.findByUser(user);

        bonusScore.setSum(bonusScore.getSum().add(sum));
        repository.save(bonusScore);
    }

    @Transactional
    public BigDecimal expendBonus(Order order) {
        BonusScore bonusScore = repository.findByUser(order.getUser());

        BigDecimal bonusSum = bonusScore.getSum();
        BigDecimal orderSum = order.getSum();

        if (bonusSum.compareTo(new BigDecimal(0)) == 0) return orderSum;

        BigDecimal paid;

        if (bonusSum.compareTo(orderSum) >= 0 ) {
            System.out.println("Заказ полностью оплачен бонусами");
            bonusScore.setSum(bonusSum.subtract(orderSum));
            paid = new BigDecimal(0);
        } else {
            System.out.println("Заказ частично оплачен бонусами");
            bonusScore.setSum(new BigDecimal(0));
            paid = orderSum.subtract(bonusSum);
        }

        repository.save(bonusScore);
        return paid;
    }

    public BonusScore getBonus(String username) {
        return repository.findByUser(userRepository.getReferenceByUsername(username));
    }
}
