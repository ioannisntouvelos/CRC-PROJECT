
import java.util.ArrayList;
import java.util.List;

public class Crc {

    private  static ArrayList<Integer> message_send = new ArrayList<>();
    private  static ArrayList<Integer> initial_message = new ArrayList<>();
    private  static ArrayList<Integer> message_received = new ArrayList<>();
    private  static ArrayList<Integer> pnumber = new ArrayList<>();
    private  static ArrayList<Integer> crc = new ArrayList<>() ;



   private void message_generator(int k){

            message_send.add(1);
            for(int i=1;i<k;i++){
                double prob = Math.random()*100;
                if(prob>=50){
                        message_send.add(1);
                }
                else{
                    message_send.add(0);
                }
            }
       System.out.print("Message is :");
       System.out.println(message_send);
    }




    private void xor_function(int [] messagepart ){

        List<Integer> temp = new ArrayList<>();


        for(int i=messagepart.length-1;i>=0;i--){
            if(messagepart[i]==pnumber.get(i)){
                temp.add(0);
            }
            else{
                temp.add(1);
            }
        }
        for(int i=temp.size()-1;i>=0;i--){
            crc.add(temp.get(i));
        }

    }

    private void division(ArrayList<Integer> message, boolean check ){
        boolean found1=false;
       if(!check){
             for(int i:message_send){
               initial_message.add(i);
            }
       }
        int size=0,sizedif=0,message_index=pnumber.size();
        int index_start=0;
        int [] message_part = new int [pnumber.size()];

        if(!check){
            int number_of_zeros = pnumber.size()-1;
            for(int i=0;i<number_of_zeros;i++){ // add zeros at the end
                message.add(0);
            }
        }
        for(int i=0;i<pnumber.size();i++){
            message_part[i]=message.get(i);
        }

        xor_function(message_part);

       while(message_index<=message.size()){


            for(int i =0 ; i<crc.size(); i++){
                if(crc.get(i)==1 ){
                   size++;
                   if(!found1){
                       index_start=i;
                   }
                   found1=true;
                }
                else if(crc.get(i)==0 && found1){
                    size++;
                }
            }
            if(!found1){
                index_start=pnumber.size();
            }
            sizedif=crc.size()-size;


            if(sizedif>0){
                for(int i=0; i <sizedif && message_index <message.size(); i++){
                    crc.add(message.get(message_index));
                    message_index++;
                }
            }
           System.out.print("CRC IS:");
           System.out.println(crc);

            if( message_index==message.size()){
                break;
            }
            create_message_part(index_start);
            found1=false;
            size=0;
        }

        if(check){
            check_result();
        }

    }

    private void create_message_part(int index ){
        int[] message_part = new int[pnumber.size()];
        int j=0;

        for(;j<pnumber.size() ;index++,j++){
            message_part[j]=crc.get(index);
        }

        crc.clear();
        xor_function(message_part);
    }


    private void check_result(){
       boolean found_error=false;
       boolean found_error_crc=false;
            for (int i=0;i<initial_message.size();i++){
                if(!initial_message.get(i).equals(message_received.get(i))){
                    System.out.println("There is an error");
                    found_error=true;
                    break;
                }
            }
            for(int i:crc) {
                if (i == 1) {
                    System.out.println("Error detected by crc");
                    found_error_crc=true;
                    break;
                }
            }
            if(!found_error){
                System.out.println("No error!");
            }
            if(found_error && !found_error_crc){
                System.out.println("Crc didnt find the error");
            }



    }


    private void send_message(){
       ArrayList<Integer> temp = new ArrayList<>();
        double prob;
        int index = crc.size();
        System.out.println("");
        for(int j=pnumber.size()-1;j>0;j--,index-- ){
            temp.add(crc.get(index-1));
        }
        index=temp.size();
        for(int j=pnumber.size()-1;j>0;j--,index-- ){
            initial_message.add(temp.get(index-1));
        }


        crc.clear();

        System.out.println(initial_message);

        for(int i : initial_message){
            prob = Math.random(); // generates  float number between 0-1
            if(prob<(1.0/100)) {
                if (i == 1) {
                    message_received.add(0);
                }
                if (i == 0) {
                    message_received.add(1);
                }
            }
                else{
                    message_received.add(i);
                }

        }
        System.out.println(message_received);




    }




    public static void main (String args []) {

        pnumber.add(1);
        pnumber.add(1);
        pnumber.add(0);
        pnumber.add(1);
        pnumber.add(0);
        pnumber.add(1);
        Crc obj1 = new Crc();
        obj1.message_generator(10);
        obj1.division(message_send, false);
        obj1.send_message();
        obj1.division(message_received, true);
      /*  for (int i = 0; i < 2; i++) {
            obj1.message_generator(10);
            obj1.division(message_send, false);
            obj1.send_message();
            obj1.division(message_received, true);
            initial_message.clear();
            message_send.clear();
            message_received.clear();

    }*/
    }
}
