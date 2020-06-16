
import java.util.ArrayList;
import java.util.List;

public class Crc {

    private  static ArrayList<Integer> message_send = new ArrayList<>();
    private  static ArrayList<Integer> initial_message = new ArrayList<>();
    private  static ArrayList<Integer> message_received = new ArrayList<>();
    private  static ArrayList<Integer> pnumber = new ArrayList<>();
    private  static ArrayList<Integer> crc = new ArrayList<>() ;
    private  static int messages_with_error =0;
    private static int messages_with_error_detected_by_crc =0;
    private static int messages_with_error_not_detected_by_crc =0;


   private void message_generator(int k){


            for(int i=0;i<k;i++){
                double prob = Math.random()*100;
                if(prob>=50){
                        message_send.add(1);
                }
                else{
                    message_send.add(0);
                }
            }


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

           if(message_index==message.size()){
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


        for( int j=0;j<pnumber.size() ;index++,j++){
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
                    messages_with_error++;
                    found_error=true;
                    break;
                }
            }
            for(int i:crc) {
                if (i == 1) {
                    messages_with_error_detected_by_crc++;
                    found_error_crc=true;
                    break;
                }
            }
            if(found_error && !found_error_crc){
                messages_with_error_not_detected_by_crc++;
            }



    }


    private void send_message(){
       ArrayList<Integer> temp = new ArrayList<>();
        double prob;
        int index = crc.size();
        for(int j=pnumber.size()-1;j>0;j--,index-- ){
            temp.add(crc.get(index-1));
        }
        index=temp.size();
        for(int j=pnumber.size()-1;j>0;j--,index-- ){
            initial_message.add(temp.get(index-1));
        }


        crc.clear();


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

    }




    public static void main (String args []) {

        pnumber.add(1);
        pnumber.add(1);
        pnumber.add(0);
        pnumber.add(1);
        pnumber.add(0);
        pnumber.add(1);
        Crc obj1 = new Crc();
        for (int i = 0; i < 100000; i++) {
            obj1.message_generator(10);
            obj1.division(message_send, false);
            obj1.send_message();
            obj1.division(message_received, true);
            initial_message.clear();
            message_send.clear();
            message_received.clear();
            crc.clear();

    }
        System.out.println("Messages with error :" + messages_with_error);
        System.out.println("Messages with error detected :" + messages_with_error_detected_by_crc);
        System.out.println("Messages with error not detected :" + messages_with_error_not_detected_by_crc);

    }
}
