package utils;

/**
 * Created by juntao on 7/28/16.
 */
public class RunThreadForInfo extends Thread{
    private String result;
    private String url;
    {
        result = "";
    }
    public RunThreadForInfo(String url){
        this.url = url;
    }
    public void run() {

        result = (new HttpUtil()).getJsonContent(url);

    }
    public String getRunLog(){
        return this.result;
    }
}