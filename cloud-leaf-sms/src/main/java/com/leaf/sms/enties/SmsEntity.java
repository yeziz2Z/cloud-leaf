package com.leaf.sms.enties;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
public class SmsEntity implements Serializable {


    private static final long serialVersionUID = -7435150069687438574L;
    private String phoneNumber;

    private String code;

}
