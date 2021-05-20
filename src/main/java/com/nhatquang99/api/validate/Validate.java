package com.nhatquang99.api.validate;

import com.nhatquang99.api.payload.request.AuthRequest;
import com.nhatquang99.api.payload.request.BillRequest;
import com.nhatquang99.api.payload.response.GenericResponse;
import org.springframework.http.HttpStatus;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validate {

    public GenericResponse authValidate(AuthRequest authRequest) {
        GenericResponse response = new GenericResponse();
        response.setStatus(HttpStatus.BAD_REQUEST);
        if (authRequest.getUsername().length() == 0) {
            response.setMessage("Không được để trống username");
            return response;
        }

        if (authRequest.getPassword().length() < 6 || authRequest.getPassword().length() > 32) {
            response.setMessage("Độ dài mật khẩu không đúng min = 6 và max = 32");
            return response;
        }
        return null;
    }

    public GenericResponse billValidate(BillRequest billRequest) {
        GenericResponse response = new GenericResponse();
        response.setStatus(HttpStatus.BAD_REQUEST);
//        if (!validateMail(billRequest.getEmail())) {
//            response.setMessage("Định dạng mail không đúng");
//            return response;
//        }
        if (billRequest.getAddress().length() == 0) {
            response.setMessage("Không được để trống địa chỉ");
            return response;
        }
        if (billRequest.getPhoneNumber().length() == 0) {
            response.setMessage("SĐT không được để trống");
            return response;
        }
        if (billRequest.getCity().length() == 0) {
            response.setMessage("City không được để trống");
            return response;
        }
        if (billRequest.getCity().length() == 0) {
            response.setMessage("City không được để trống");
            return response;
        }
        if (billRequest.getDetails().size() == 0) {
            response.setMessage("Không có sản phẩm trong giỏ hàng");
            return response;
        }
        return null;
    }

    public boolean validateMail(String email) {
        final String EMAIL_REGEX = "^[\\w-\\+]+(\\.[\\w]+)*@[\\w-]+(\\.[\\w]+)*(\\.[a-z]{2,})$";
        Pattern pattern = Pattern.compile(EMAIL_REGEX, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
