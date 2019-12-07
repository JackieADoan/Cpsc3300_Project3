public enum StAcc {
    R("read"),
    W("write"),
    hit("hit"),
    miss("miss");

    private String str;
    StAcc(String Stacc){
        str = Stacc;
    }
	//Determines Read or Write access of Cache
    //and Memory Hit/Miss
    public static StAcc getAccess(String str){
        StAcc Stacc = null;
        if(str.toUpperCase().equals("R")){
            Stacc = R;
		} else if(str.toUpperCase().equals("W")){
            Stacc = W;
        } else if(str.toLowerCase() == "hit"){
            Stacc = hit;
        } else if(str.toLowerCase() == "miss"){
            Stacc = miss;
        }
        return Stacc;
    }
    public String toString(){
        return str;
    }
}
