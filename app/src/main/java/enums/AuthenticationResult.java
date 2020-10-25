package enums;

public enum AuthenticationResult {
    ERROR(0),
    SUCCESSFUL(1),
    FAILED(2),
    UNKNOWN(3);

    private String mResultName;
    private int mResultNum;

    AuthenticationResult(int num) {
        this.mResultNum = num;
    }

    public String bytName() {
        switch (this.mResultNum) {
            case 0:
                this.mResultName = "ERROR";
                break;
            case 1:
                this.mResultName = "SUCCESSFUL";
                break;
            case 2:
                this.mResultName = "FAILED";
                break;
            case 3:
                this.mResultName = "UNKNOWN";
                break;
        }
        return this.mResultName;
    }

    public int byNum() {
        return this.mResultNum;
    }
}

