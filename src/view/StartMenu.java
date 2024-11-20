package view;

public enum StartMenu
{
    START_WORKING(1, " Start currency exchange "),
    FIND_TRANS(2," Find transaction by number "),
    ALL_TRANS (3, " General list of transactions "),
    TRANS_REPORT_BY_DATE (4, " List of transactions by date "),
    CURRENCY_REPORT (5, " Report on all currencies "),
    REMOVE_TRANS (6, " Remove transaction "),
    EXIT (7, " Close currency exchange ")
    ;

    private int number;
    private String action;

    StartMenu(int number, String action) {
        this.number = number;
        this.action = action;
    }

    public int getNumber() {
        return number;
    }

    public String getAction() {
        return action;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Task{");
        sb.append("number=").append(number);
        sb.append(", action='").append(action).append('\'');
        sb.append('}');
        return sb.toString();
    }

    public static void printMenu (){
        System.out.println("\n=================== Choose action by pressing number 1 - 7: ===================\n ");
        StartMenu myO [] = StartMenu.values();
        for (Object o : myO) {
            System.out.println(o);
        }//end for
        System.out.println();

    }//end printMenu

}//end enum