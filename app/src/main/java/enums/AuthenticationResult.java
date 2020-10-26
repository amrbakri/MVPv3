package enums;

public enum AuthenticationResult {
    ERROR(0),
    SUCCESSFUL(1),
    FAILED(2);
    //UNKNOWN(3);

    private String mResultName;
    private int mResultByNum;

    AuthenticationResult(int num) {
        this.mResultByNum = num;
    }

    public String bytName() {
        switch (this.mResultByNum) {
            case 0:
                this.mResultName = "ERROR";
                break;
            case 1:
                this.mResultName = "SUCCESSFUL";
                break;
            case 2:
                this.mResultName = "FAILED";
                break;
            /*case 3:
                this.mResultName = "UNKNOWN";
                break;*/
        }
        return this.mResultName;
    }

    public int byNum() {
        return this.mResultByNum;
    }
}

