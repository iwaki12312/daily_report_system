package models.validators;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import models.Client;
import utils.DBUtil;

public class ClientValidator {

    public static List<String> validate(Client c,
                                          Boolean code_duplicate_check_flag ,
                                          Boolean address_duplicate_check_flag,
                                          Boolean tell_duplicate_check_flag) {

        List<String> errors = new ArrayList<String>();

        String code_error = _validateCode(c.getCode(), code_duplicate_check_flag);
        if(!code_error.equals("")) {
            errors.add(code_error);
        }

        String name_error = _validateName(c.getName());
        if(!name_error.equals("")) {
            errors.add(name_error);
        }

        String address_error = _validateAddress(c.getAddress(), address_duplicate_check_flag);
        if(!address_error.equals("")) {
            errors.add(address_error);
        }

        String tell_error = _validateTell(c.getTell(), tell_duplicate_check_flag);
        if(!tell_error.equals("")) {
            errors.add(tell_error);
        }


        return errors;
    }

    // 取引先コードのチェック
    private static String _validateCode(String code, Boolean code_duplicate_check_flag) {
        // 必須入力チェック
        if(code == null || code.isEmpty()) {
            return "社員番号を入力してください。";
        }

        // すでに登録されている取引先コードとの重複チェック
        if(code_duplicate_check_flag) {
            EntityManager em = DBUtil.createEntityManager();
            long clients_count = (long)em.createNamedQuery("checkRegisteredClientCode", Long.class)
                                           .setParameter("code", code)
                                             .getSingleResult();
            em.close();
            if(clients_count > 0) {
                return "入力された取引先コードの情報はすでに存在しています。";
            }
        }

        return "";
    }

    // 取引先名の必須入力チェック
    private static String _validateName(String name) {
        if(name == null || name.isEmpty()) {
            return "名称を入力してください。";
        }

        return "";
    }

    // 住所チェック
    private static String _validateAddress(String address,Boolean address_duplicate_check_flag) {
        if(address == null || address.isEmpty()) {
            return "住所を入力してください。";
        }

        // すでに登録されている住所との重複チェック
        if(address_duplicate_check_flag) {
            EntityManager em = DBUtil.createEntityManager();
            long clients_count = (long)em.createNamedQuery("checkRegisteredClientAddress", Long.class)
                                           .setParameter("address", address)
                                             .getSingleResult();
            em.close();
            if(clients_count > 0) {
                return "入力された住所の取引先はすでに存在しています。";
            }
        }

        return "";
    }

    // 電話番号チェック
    private static String _validateTell(String tell , Boolean tell_duplicate_check_flag) {
        if(tell == null || tell.isEmpty()) {
            return "電話番号を入力してください。";
        }

        // すでに登録されている電話番号との重複チェック
        if(tell_duplicate_check_flag) {
            EntityManager em = DBUtil.createEntityManager();
            long clients_count = (long)em.createNamedQuery("checkRegisteredClientTell", Long.class)
                                           .setParameter("tell", tell)
                                             .getSingleResult();
            em.close();
            if(clients_count > 0) {
                return "入力された電話番号の取引先はすでに存在しています。";
            }
        }

        return "";
    }
}
