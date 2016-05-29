package com.example.oscar.travelagent2;



import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class App_Handler {

    private Context context;

    public App_Handler(Context context){
        this.context = context;
    }

    public  boolean HaveInternet() {
        boolean result;
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connManager.getActiveNetworkInfo();
        result = !(info == null || !info.isConnected()) && info.isAvailable();
        return result;
    }
    public boolean isMember(){
        boolean pass;
        Member member = MainActivity.User;
        pass = member != null;
        return pass;
    }
    public boolean just(String word){
        boolean pass = true;
        if(word.equals("")){
            pass = false;
        }else{
            String[] components = word.split("|");
            for(String com:components){
                switch (com){
                    case "!":
                        pass = false;
                        break;
                    case "@":
                        pass = false;
                        break;
                    case "#":
                        pass = false;
                        break;
                    case "$":
                        pass = false;
                        break;
                    case "%":
                        pass = false;
                        break;
                    case "^":
                        pass = false;
                        break;
                    case "&":
                        pass = false;
                        break;
                    case "*":
                        pass = false;
                        break;
                    case "(":
                        pass = false;
                        break;
                    case ")":
                        pass = false;
                        break;
                    case "=":
                        pass = false;
                        break;
                    case ".":
                        pass = false;
                        break;
                    case "'":
                        pass = false;
                        break;
                    case "~":
                        pass = false;
                        break;
                    default:
                        break;
                }
            }
        }
        return pass;
    }
    public boolean ok_email(String word){
        boolean pass = false;
        if(word.equals("")){
            pass = false;
        }else{
            String[] components = word.split("|");
            for(String com:components){
                if(com.equals("@")){
                    pass = true;
                }
            }

        }
        return pass;
    }
    public String NameFix(String name){
        String[] words = name.split("|");
        String rebuilt_name;
        boolean InTaiwan = false;
        for(String word:words){
            if(word.equals("台")){
                InTaiwan = true;
                break;
            }
        }
        if(InTaiwan){
            switch (name){
                case "台北市":
                    rebuilt_name = "臺北市";
                    break;
                case "台中市":
                    rebuilt_name = "臺中市";
                    break;
                case "台南市":
                    rebuilt_name = "臺南市";
                    break;
                case "台東縣":
                    rebuilt_name = "臺東縣";
                    break;
                default:
                    rebuilt_name = name;
                    break;
            }
        }else{
            switch (name){
                case "東京都":
                    rebuilt_name = "東京";
                    break;
                case "大阪府":
                    rebuilt_name = "大阪";
                    break;
                case "胡志明區":
                    rebuilt_name = "胡志明市";
                    break;
                case "德里":
                    rebuilt_name = "新德里";
                    break;
                case "馬尼拉大都":
                    rebuilt_name = "馬尼拉";
                    break;
                case "伊斯坦堡省":
                    rebuilt_name = "伊斯坦堡";
                    break;
                case "哈巴羅夫斯區":
                    rebuilt_name = "伯利";
                    break;
                case "普列莫爾斯":
                    rebuilt_name = "海參威";
                    break;
                case "上海市":
                    rebuilt_name = "上海";
                    break;
                case "北京市":
                    rebuilt_name = "北京";
                    break;
                case "廣東省":
                    rebuilt_name = "廣州";
                    break;
                case "福建省":
                    rebuilt_name = "福州";
                    break;
                case "雲南省":
                    rebuilt_name = "昆明";
                    break;
                case "重慶市":
                    rebuilt_name = "重慶";
                    break;
                case "湖北省":
                    rebuilt_name = "武漢";
                    break;
                case "浙江省":
                    rebuilt_name = "杭州";
                    break;
                case "江蘇省":
                    rebuilt_name = "南京";
                    break;
                case "河南省":
                    rebuilt_name = "開封";
                    break;
                case "陝西省":
                    rebuilt_name = "西安";
                    break;
                case "遼寧省":
                    rebuilt_name = "瀋陽";
                    break;
                case "甘肅省":
                    rebuilt_name = "蘭州";
                    break;
                case "海南省":
                    rebuilt_name = "海口";
                    break;
                case "夏威夷":
                    rebuilt_name = "檀香山";
                    break;
                case "內華達":
                    rebuilt_name = "拉斯維加斯";
                    break;
                case "佛羅里達":
                    rebuilt_name = "邁阿密";
                    break;
                case "華盛頓":
                    rebuilt_name = "西雅圖";
                    break;
                case "哥倫比亞特區":
                    rebuilt_name = "華盛頓特區";
                    break;
                case "安大略":
                    rebuilt_name = "多倫多";
                    break;
                case "魁北克省":
                    rebuilt_name = "蒙特婁";
                    break;
                case "英屬哥倫比":
                    rebuilt_name = "溫哥華";
                    break;
                case "加利福尼亞州":
                    rebuilt_name = "洛杉磯";
                    break;
                case "伊利諾伊州":
                    rebuilt_name = "芝加哥";
                    break;
                case "紐約州":
                    rebuilt_name = "紐約";
                    break;
                case "澳大利亞首都坎培拉":
                    rebuilt_name = "坎培拉";
                    break;
                case "里約熱內盧州":
                    rebuilt_name = "里約";
                    break;
                case "日內瓦州":
                    rebuilt_name = "日內瓦";
                    break;
                case "新南威爾斯":
                    rebuilt_name = "雪梨";
                    break;
                case "維多利亞州州":
                    rebuilt_name = "墨爾本";
                    break;
                case "昆士蘭州":
                    rebuilt_name = "布里斯班";
                    break;
                default:
                    rebuilt_name = name;
                    break;
            }
        }

        return rebuilt_name;
    }
}
