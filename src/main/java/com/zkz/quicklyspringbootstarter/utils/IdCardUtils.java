package com.zkz.quicklyspringbootstarter.utils;

import com.zkz.quicklyspringbootstarter.exception.BaseException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author: dongck
 * @create: 2018-11-09 15:01
 **/
public class IdCardUtils {
    /**
     * 身份证老规则的长度
     */
    private static final int ID_CARD_LENGTH_OLD = 15;
    /**
     * 身份证新规则的长度
     */
    private static final int ID_CARD_LENGTH_NEW = 18;

    /**
     * 每位加权因子
     */
    private static int[] power = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};

    /**
     * 省，直辖市代码表： { 11:"北京",12:"天津",13:"河北",14:"山西",15:"内蒙古",
     * * 21:"辽宁",22:"吉林",23:"黑龙江",31:"上海",32:"江苏",
     * * 33:"浙江",34:"安徽",35:"福建",36:"江西",37:"山东",41:"河南",
     * * 42:"湖北",43:"湖南",44:"广东",45:"广西",46:"海南",50:"重庆",
     * * 51:"四川",52:"贵州",53:"云南",54:"西藏",61:"陕西",62:"甘肃",
     * * 63:"青海",64:"宁夏",65:"新疆",71:"台湾",81:"香港",82:"澳门",91:"国外"}
     * <p>
     * 验证所有的身份证的合法性
     *
     * @param idCard 身份证号
     * @return true or false
     */
    public static boolean isValidatedAllIdCard(String idCard) {
        if (StringUtils.isBlank(idCard)) {
            return false;
        }
        if (idCard.length() == ID_CARD_LENGTH_OLD) {
            if (!StringUtils.isNumber(idCard)) {
                return false;
            }
            // 将15位身份证变为18位
            idCard = convertIdCarTo18bit(idCard);
        }
        return isValidate18IdCard(idCard);
    }

    /**
     * <p>
     * 判断18位身份证的合法性
     * </p>
     * 根据〖中华人民共和国国家标准GB11643-1999〗中有关公民身份号码的规定，公民身份号码是特征组合码，由十七位数字本体码和一位数字校验码组成。
     * 排列顺序从左至右依次为：六位数字地址码，八位数字出生日期码，三位数字顺序码和一位数字校验码。
     * <p>
     * 顺序码: 表示在同一地址码所标识的区域范围内，对同年、同月、同 日出生的人编定的顺序号，顺序码的奇数分配给男性，偶数分配 给女性。
     * </p>
     * <p>
     * 1.前1、2位数字表示：所在省份的代码； 2.第3、4位数字表示：所在城市的代码； 3.第5、6位数字表示：所在区县的代码；
     * 4.第7~14位数字表示：出生年、月、日； 5.第15、16位数字表示：所在地的派出所的代码；
     * 6.第17位数字表示性别：奇数表示男性，偶数表示女性；
     * 7.第18位数字是校检码：也有的说是个人信息码，一般是随计算机的随机产生，用来检验身份证的正确性。校检码可以是0~9的数字，有时也用x表示。
     * </p>
     * <p>
     * 第十八位数字(校验码)的计算方法为： 1.将前面的身份证号码17位数分别乘以不同的系数。从第一位到第十七位的系数分别为：7 9 10 5 8 4
     * 2 1 6 3 7 9 10 5 8 4 2
     * </p>
     * <p>
     * 2.将这17位数字和系数相乘的结果相加。
     * </p>
     * <p>
     * 3.用加出来和除以11，看余数是多少？
     * </p>
     * 4.余数只可能有0 1 2 3 4 5 6 7 8 9 10这11个数字。其分别对应的最后一位身份证的号码为1 0 X 9 8 7 6 5 4 3
     * 2。
     * <p>
     * 5.通过上面得知如果余数是2，就会在身份证的第18位数字上出现罗马数字的Ⅹ。如果余数是10，身份证的最后一位号码就是2。
     * </p>
     */
    private static boolean isValidate18IdCard(String idCard) {
        // 非18位为假
        if (StringUtils.isBlank(idCard) || idCard.length() != ID_CARD_LENGTH_NEW) {
            return false;
        }
        // 获取前17位
        String idCard17 = idCard.substring(0, 17);
        // 获取第18位
        String idCard18Code = idCard.substring(17, 18);

        // 前17位是否都为数字
        if (!StringUtils.isNumber(idCard17)) {
            return false;
        }

        // 身份证前17位转数字数组
        int[] bit = conversionCharToInt(idCard17.toCharArray());
        // 将和值与11取模得到余数进行校验码判断
        String checkCode = getCheckCodeBySum(getPowerSum(bit));
        // 将身份证的第18位与算出来的校码进行匹配，不相等就为假
        return idCard18Code.equalsIgnoreCase(checkCode);
    }

    /**
     * 将15位的身份证转成18位身份证
     *
     * @param oldIdCard 15身份证号
     * @return 18位身份证号
     */
    private static String convertIdCarTo18bit(String oldIdCard) {
        // 非15位身份证
        if (oldIdCard.length() != ID_CARD_LENGTH_OLD) {
            throw new BaseException("0401");
        }
        //验证身份证是否全是数字
        if (!StringUtils.isNumber(oldIdCard)) {
            throw new BaseException("0404");
        }
        // 获取出生年月日
        String birthday = oldIdCard.substring(6, 12);
        Date formatBirthday;
        try {
            formatBirthday = new SimpleDateFormat("yyMMdd").parse(birthday);
        } catch (ParseException e) {
            throw new BaseException("0402");
        }
        // 获取15位身份证的年份
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(formatBirthday);
        String year = String.valueOf(calendar.get(Calendar.YEAR));

        //拼成17位身份证
        String result = oldIdCard.substring(0, 6) + year + oldIdCard.substring(8);

        // 将身份证17位转为整型数组
        int[] bit = conversionCharToInt(result.toCharArray());
        // 将身份证的每位和对应位的加权因子相乘之后，再得到和值
        int sum17 = getPowerSum(bit);
        // 获取和值与11取模得到余数进行校验码与第18位校验码拼接
        return result.concat(getCheckCodeBySum(sum17));
    }

    /**
     * 将身份证前17的每位和对应位的加权因子相乘之后，再得到和值
     */
    private static int getPowerSum(int[] bit) {
        int sum = 0;

        if (power.length != bit.length) {
            throw new BaseException("0405");
        }

        for (int i = 0; i < bit.length; i++) {
            for (int j = 0; j < power.length; j++) {
                if (i == j) {
                    sum = sum + bit[i] * power[j];
                }
            }
        }
        return sum;
    }

    /**
     * 将和值与11取模得到余数进行校验码判断
     */
    private static String getCheckCodeBySum(int sum17) {
        String checkCode;
        switch (sum17 % 11) {
            case 10:
                checkCode = "2";
                break;
            case 9:
                checkCode = "3";
                break;
            case 8:
                checkCode = "4";
                break;
            case 7:
                checkCode = "5";
                break;
            case 6:
                checkCode = "6";
                break;
            case 5:
                checkCode = "7";
                break;
            case 4:
                checkCode = "8";
                break;
            case 3:
                checkCode = "9";
                break;
            case 2:
                checkCode = "x";
                break;
            case 1:
                checkCode = "0";
                break;
            case 0:
                checkCode = "1";
                break;
            default:
                throw new BaseException("0403");
        }
        return checkCode;
    }

    /**
     * 将字符数组转为整型数组
     */
    private static int[] conversionCharToInt(char[] c) throws NumberFormatException {
        int[] a = new int[c.length];
        int k = 0;
        for (char temp : c) {
            a[k++] = Integer.parseInt(String.valueOf(temp));
        }
        return a;
    }
}
