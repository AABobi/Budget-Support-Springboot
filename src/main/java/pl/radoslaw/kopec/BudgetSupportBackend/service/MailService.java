package pl.radoslaw.kopec.BudgetSupportBackend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import pl.radoslaw.kopec.BudgetSupportBackend.model.User;
import pl.radoslaw.kopec.BudgetSupportBackend.repository.UserRepository;


@Service
public class MailService {
    private JavaMailSender javaMailSender;
    @Autowired
    public UserRepository usersRepository;

    @Autowired
    public MailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendEmail(User user) throws MailException {
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(user.getEmail());
        mail.setSubject("Budget support application");
        String mainUrl = "http://localhost:4200/RegisterUserComponent/" + user.getConfirm();
        mail.setText(mainUrl);
        this.javaMailSender.send(mail);
    }
}