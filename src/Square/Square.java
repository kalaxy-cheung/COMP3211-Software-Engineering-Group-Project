package Square;

public class Square {
    protected int position;
    protected int type;


    public Square(){
        this.type = 0;
        this.position = 0;
    }

    public int getPosition(){
        return position;
    }

    public void setPosition(int position){
        this.position = position;
    }

    public int getType(){
        return type;
    }

    public void setType(int type){
        this.type = type;
    }


}