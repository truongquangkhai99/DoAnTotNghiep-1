package com.nhatquang99.api.service.impl;

import com.nhatquang99.api.config.MyConstants;
import com.nhatquang99.api.model.PasswordResetToken;
import com.nhatquang99.api.model.User;
import com.nhatquang99.api.payload.response.GenericResponse;
import com.nhatquang99.api.repository.PasswordResetTokenRepository;
import com.nhatquang99.api.repository.UserRepository;
import com.nhatquang99.api.service.IMailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Calendar;
import java.util.Random;

@Service
public class MailServiceImpl implements IMailService {
    @Autowired
    public JavaMailSender emailSender;

    @Autowired
    private PasswordResetTokenRepository passwordTokenRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public GenericResponse confirmCreateAccount(String mail) throws MessagingException {
        String body =
                "<div style='max-width:600px;margin:1.5rem auto 0;padding:0 16px'>" +
                        "<h2 style='color:#2ab27b;margin-bottom:12px;line-height:26px'>Hello!</h2>" +
                        "<p style='font-size:18px;line-height:24px;margin:0 0 16px' align='justify'>Thank you for signing up for Store. We're really happy to have you! Click the link below to confirm your account!</p>" +
                        "<p style='font-size:18px;line-height:24px;margin:2rem auto' align='center'>" +
                        "<a href='" + MyConstants.MY_URL + "/api/v1/mail/register/confirm/" + mail + "' style='font-weight:normal;color:white;display:inline-block;background-color:#2ab27b;border-radius:4px;letter-spacing:1px;font-size:20px;line-height:26px;text-decoration:none;word-break:break-word;padding:14px 32px' target='_blank' data-saferedirecturl='https://www.google.com/url?q=http://2iu6.mj.am/lnk/AMoAAJmcYvoAAAAAAAAAAR4GBv4AAAAAAAEAAAAAAAMqMABgTohHO66gd2lWRJmSE7-6FIIIRAADB78/2/6Wxmpnu9Hf9DJN6slXNPVw/aHR0cHM6Ly9hdXRoLnNjYWxpbmdvLmNvbS91c2Vycy9jb25maXJtYXRpb24_Y29uZmlybWF0aW9uX3Rva2VuPXlSZC1UcnpwOW51RlNqQVdxaDJ1&amp;source=gmail&amp;ust=1615845846149000&amp;usg=AFQjCNEYdp9SysXbF0QWoTDgzld9IODJSg'>Confirm my account</a>\n" +
                        "</p>" +
                        "<p style='font-size:18px;line-height:24px;margin:0 0 16px' align='justify'>Need more an introduction about us? See " +
                        "<a href=" + MyConstants.MY_URL + "' style='font-weight:bold;color:#439fe0;text-decoration:none;word-break:break-word' target='_blank' data-saferedirecturl='https://www.google.com/url?q=http://2iu6.mj.am/lnk/AMoAAJmcYvoAAAAAAAAAAR4GBv4AAAAAAAEAAAAAAAMqMABgTohHO66gd2lWRJmSE7-6FIIIRAADB78/3/r5m0rPdPIein_mL-ZBqKzw/aHR0cHM6Ly9zY2FsaW5nby5jb20vbWFnaWM&amp;source=gmail&amp;ust=1615845846149000&amp;usg=AFQjCNHLq1TY5aKjwRsieAQgxFBNDPxCwQ'>name website</a>!\n" +
                        "</p>" +
                        "<p style='font-size:18px;line-height:24px;margin:0 0 16px' align='justify'>And lastly, tell us your feedback good and bad, we want it all :) <br> Reply to this email or write to " +
                        "<a href='mailto:nguyenhnhatquang@gmail.com' style='font-weight:bold;color:#439fe0;text-decoration:none;word-break:break-word' target='_blank'>nguyenhnhatquang@gmail.com</a>." +
                        "</p>" +
                        "</div>";
        String subject = "Confirmation instructions";
        MimeMessage mailMessage = constructEmail(subject, body, mail);
        Thread thread = new Thread(() -> {
            emailSender.send(mailMessage);
        });
        thread.start();
        return new GenericResponse();
    }

    @Override
    public GenericResponse validateMailRegister(String email) {
        try {
            User user = userRepository.findByEmail(email).get();
            user.setVerifyMail(true);
            userRepository.save(user);
            return new GenericResponse();
        } catch (RuntimeException exception) {
            return new GenericResponse();
        }
    }

    @Override
    public GenericResponse validatePasswordResetToken(String email, String token, String password) {
        PasswordResetToken passToken = passwordTokenRepository.findByToken(token);
        if ((passToken == null) || !(passToken.getUser().getEmail().equals(email))) {
            return new GenericResponse();
        }

        Calendar cal = Calendar.getInstance();
        if ((passToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
            return new GenericResponse();
        }

        // Cập nhật mật khẩu mới
        User user = passToken.getUser();
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
        return new GenericResponse();
    }

    @Override
    public GenericResponse createPasswordResetTokenForUser(String email, String token) {
        try {
            User user = userRepository.findByEmail(email).get();
            PasswordResetToken myToken = new PasswordResetToken(token, user);
            passwordTokenRepository.save(myToken);

            String newPassword = randomPassword();
            String body =
                    "<div style='max-width:600px;margin:1.5rem auto 0;padding:0 16px'>" +
                            "<h2 style='color:#2ab27b;margin-bottom:12px;line-height:26px'>Hello " + user.getFullName() + "!</h2>" +
                            "<p style='font-size:18px;line-height:24px;margin:0 0 16px' align='justify'>Someone has requested a link to change your password.<Br> Your password is below.</p>" +
                            "<p style='font-size:18px;line-height:24px;margin:2rem auto' align='center'>" +
                            "<p style='font-weight:normal;color:white;display:inline-block;background-color:#2ab27b;border-radius:4px;letter-spacing:1px;font-size:20px;line-height:26px;text-decoration:none;word-break:break-word;padding:14px 32px' target='_blank' data-saferedirecturl='https://www.google.com/url?q=http://2iu6.mj.am/lnk/AMoAAJmcYvoAAAAAAAAAAR4GBv4AAAAAAAEAAAAAAAMqMABgTohHO66gd2lWRJmSE7-6FIIIRAADB78/2/6Wxmpnu9Hf9DJN6slXNPVw/aHR0cHM6Ly9hdXRoLnNjYWxpbmdvLmNvbS91c2Vycy9jb25maXJtYXRpb24_Y29uZmlybWF0aW9uX3Rva2VuPXlSZC1UcnpwOW51RlNqQVdxaDJ1&amp;source=gmail&amp;ust=1615845846149000&amp;usg=AFQjCNEYdp9SysXbF0QWoTDgzld9IODJSg'>" + newPassword + "</a>" +
                            "</p>" +
                            "<p style='font-size:18px;line-height:24px;margin:0 0 16px' align='justify'> Your password won't change until you " +
                            "<a href='" + MyConstants.MY_URL + "/api/v1/mail/password/confirm/" + email + "/?token=" + token + "&password=" + newPassword + "' style='font-weight:bold;color:#439fe0;text-decoration:none;word-break:break-word' target='_blank'>click here</a>" +
                            "</p>" +
                            "<p style='font-size:18px;line-height:24px;margin:0 0 16px' align='justify'>This mail is only available for 20 minutes<Br>If you didn't request this, please ignore this email.  </p>" +
                            "</div>";

            String subject = "Reset password instructions";
            MimeMessage mailMessage = constructEmail(subject, body, email);
            Thread thread = new Thread(() -> {
                emailSender.send(mailMessage);
            });
            thread.start();
            return new GenericResponse();
        } catch (RuntimeException | MessagingException ex) {
            return new GenericResponse();
        }
    }


    private MimeMessage constructEmail(String subject, String body, String mailReceive) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        boolean multipart = true;
        MimeMessageHelper helper = new MimeMessageHelper(message, multipart, "utf-8");
        String htmlMsg = body;
        message.setContent(htmlMsg, "text/html");
        helper.setTo(mailReceive);
        helper.setSubject(subject);
        helper.setFrom(MyConstants.MY_EMAIL);

        return message;
    }

    private String randomPassword() {
        int leftLimit = 48; // số '0'
        int rightLimit = 122; // ký tự 'z'
        int targetStringLength = 8; // độ dài chuỗi
        Random random = new Random();

        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97)) // lọc các ký tự không phải là số hoặc ký tự alphabet
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

        return generatedString;
    }
}
