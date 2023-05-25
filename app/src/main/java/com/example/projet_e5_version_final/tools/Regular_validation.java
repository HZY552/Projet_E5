package com.example.projet_e5_version_final.tools;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Regular_validation {

    private Pattern pattern;
    private Matcher matcher;

    public boolean check_email(final String email){
        String EMAIL_PATTERN =
                "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);

        return matcher.matches();

    }


}
