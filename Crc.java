/**
  Author : Ioannis Ntouvelos
  AEM : 3340
  email : ntouveloi@csd.auth.gr
 **/


import java.util.ArrayList;


public class Crc {

    private  static ArrayList<Integer> message_send = new ArrayList<>();// Arraylist <Integer >  για αποθήκευση και χρήση ως αρχικό μήνυμα
    private  static ArrayList<Integer> initial_message = new ArrayList<>();// Arraylist <Integer >  για αποθήκευση του αρχικού μηνύματος πριν από αλλαγές (το χρειαζόμαστε στο τέλος για τους ελέγχους)
    private  static ArrayList<Integer> message_received = new ArrayList<>(); // Arraylist <Integer >  για αποθήκευση του μηνύματος που φτάνει στον δέκτη
    private static int [] pnumber = new int[6]; // πίνακας για αποθήκευση του κλειδιού P
    private static int [] crc = new int[6]; // πίνακας για αποθήκευση του Crc
    private static int number_of_messages=1000000000; // μεταβλητή για το ποσό των μηνυμάτων που θέλουμε να εξετάσει το πρόγραμμα
    private  static float messages_with_error =0; // μεταβλητή για το ποσό των μηνυμάτων που έφτασαν με λάθος
    private static float messages_with_error_detected_by_crc =0;// μεταβλητή για το ποσό των μηνυμάτων που ανιχνεύθηκαν ως λάθος απο το CRC
    private static float messages_with_error_not_detected_by_crc =0; // μεταβλητή για το ποσό των μηνυμάτων που έφτασαν με λάθος αλλά δεν ανιχνεύθηκαν ως λάθος

/**Συνάρτηση για δημιουργία τυχάιων μηνυμάτων , παίρνει όρισμα έναν ακέραιο για το ποσό των bits που θέλουμε να έχει το μήνυμα  **/
   private void message_generator(int k){


            for(int i=0;i<k;i++){
                double prob = Math.random()*100; // επιστρέφει "τυχαίο " αριθμό στο διάστημα 0-100
                if(prob>=50){
                        message_send.add(1);
                }
                else{
                    message_send.add(0);
                }
            }

    //  System.out.println(message_send);
    }



/**Υλοποίηση της συνάρτησης xor , παίρνει όρισμα δύο ακέραιους κι αν είναι ίσοι επιστρέφει 0 αλλιώς επιστρέφει 1 **/

   private int xor_function(int a , int b  ){

       if(a==b){
           return 0;
       }
       else{
           return 1;
       }
   }



/**Η συνάρτηση division  παίρνει όρισματα : μια Arraylist<Integer> που είναι το μήνυμα μας και μια λογική μεταβλητή check
 * μέσω της οποιάς ξεχωρίζουμε αν καλείται η συνάρτηση από την πλευρά του πομπού για εύρεση του CRC(check=false) ή από την πλευρά του
 * δέκτη για έλεγχο ύπαρξης λάθους(check=true). **/

    private void division(ArrayList<Integer> message, boolean check ) {
        boolean found1 = false;
        /*Αντιγραφή του μηνύματος ώστε να το κρατήσουμε ανέπαφο για τους ελέγχους στο τέλος*/
        if (!check) {
            for (int i : message_send) {
                initial_message.add(i);
            }
        }
        /* index που δίνει σε ποιο σημείο του μηνύματος έχουμε φτάσει ώστε να "κατεβάσουμε" το επόμενο bit ,χρησιμοποιείται κι ως
        * συνθήκη τερματισμού αν έχουμε φτάσει στο τέλος του μηνύματος.*/
        int  message_index = pnumber.length;

        /* Προσθήκη των μηδενικών στο τέλος του μηνύματος όταν βρισκόμαστε στο πομπό και ψάχνουμε το CRC (check=false )*/
       if (!check) {
            int number_of_zeros = pnumber.length - 1;
            for (int i = 0; i < number_of_zeros; i++) {
                message.add(0);
            }
        }

       /*Υλοποίηση της πρώτης πράξης xor έξω από την επανάληψη  για να αρχικοποιηθεί ο πίνακας crc */

        for (int i = 0; i < pnumber.length; i++) {
            crc[i] = xor_function(message.get(i),pnumber[i]);
        }
        /*Στην συνέχεια αρχίζει η επανάληψη με συνθήκη τερματισμού όσο δεν έχουν τελειώσει τα στοιχεία του μηνύματος .
        * Ελέγχουμε το πρώτο στοιχείου του πίνακα crc περιέχει κάθε φορά το αποτέλεσμα της προηγούμενης πράξης .
        * Αν είναι μηδέν τότε το διώχνουμε κάνοντας την πράξη xor 0 με όλα τα υπόλοιπα στοιχεία και κατεβάζουμε το επόμενο
        * στοιχείο από το μήνυμα , στην ουσία υλοποιούμε έτσι την μετακίνηση των στοιχείων μια θέση αριστερά. Αν το στοιχείο
        * είναι ένα τότε κάνουμε κανονικά πράξη xor με τα στοιχεία του κλειδιού .*/

        while (message_index < message.size()) {
          // System.out.print("Remainder is :");

            if (crc[0]==1){
                for(int i=0; i <pnumber.length;i++){
                    crc[i]=xor_function(crc[i],pnumber[i]);
                  ///  System.out.print(crc[i]);
                }
            }
            else {
                for(int i=1;i<pnumber.length;i++){
                    crc[i-1]=xor_function(crc[i],0);
                  //  System.out.print(crc[i-1]);
                }
                crc[pnumber.length-1]=message.get(message_index); // "Κατεβάζουμε" το ψηφίο από το μήνυμα
                message_index++;
               // System.out.println(crc[pnumber.length-1]);
            }


        }


        /*Μόλις τελειώσει η επανάληψη λαμβάνουμε υπόψιν και μια ακόμη περίπτωση , αν το τελικό αποτέλεσμα ξεκινάει με 1
        * τότε κάνουμε ξανά μια τελευταία πράξη ώστε να καταλήξουμε στο τελικό 5ψήιο CRC*/
        if(crc[0]==1){
            for(int i=0;i<crc.length;i++){
                crc[i]=xor_function(crc[i],pnumber[i]);
            }
        }
       /* System.out.print("CRC IS :");
        for(int i=0;i<crc.length;i++){
            System.out.print(crc[i]);
        }*/
        //System.out.println("");


        // Άμα είμαστε στην πλευρά του δέκτη (check=true ) καλόυμε την συνάρτηση για τον έλεγχο των αποτελεσμάτων
        if (check) {
            check_result();
        }
    }


/**Η συνάρτηση check_result ελέγχει τα αποτελέσματα μετά από κάθε : δημιουργία , αποστολή και παραλαβή ενός μηνύματος
 * και κρατάει  τα στατιστικά ώστε να δημιουργηθούν τα ποσοστά στην main στην συνέχεια . Συγκεκριμένα ελέγχει πόσα
 * μηνύματα έφτασαν με λάθος , πόσα ανιχνεύθηκαν ως λανθασμένα από CRC και πόσα ήταν λάθος αλλά δεν ανιχνεύθηκαν. **/

    private void check_result(){
       boolean found_error=false;
       boolean found_error_crc=false;

        //Έλεγχος αν υπήρξε κάποιο λάθος στην αποστολή του μηνύματος
        // Εδώ χρησιμοποιούμε το αντίγραφο του αρχικού μηνύματος για σύγκριση με αυτό που έφτασε)
            for (int i=0;i<initial_message.size();i++){
                if(!initial_message.get(i).equals(message_received.get(i))){
                    messages_with_error++;
                    found_error=true;
                    break;
                }
            }
            // Έλεγχος περίπτωσης για μήνυμα που ανιχνεύθηκε ως λάθος από το CRC
            for(int i:crc) {
                if (i == 1) {
                    messages_with_error_detected_by_crc++;
                    found_error_crc=true;
                    break;
                }
            }
            // Έλεγχος περίπτωσης για μήνυμα που ήταν λάθος αλλά δεν ανιχνεύθηκε
            if(found_error && !found_error_crc){
                messages_with_error_not_detected_by_crc++;
            }



    }


    /**Η συνάρτηση send_message υλοποιεί την αποστολή ενός μηνύματος , συγκεκριμένα προσθέτει στο τέλος του αρχικού
     * μηνύματος τον κώδικα CRC και στην συνέχεια με BER 1/100 μεταφέρει το μήνυμα(μεταφορά σε άλλον πίνακα που
     * αντιπροσωπεύει τον παραλήπτη ) .**/

 private void send_message(){





    // System.out.print("CRC I AM SENDING : ");
     double prob;
     //Προσθήκη του CRC στο τέλος του αρχικόυ μηνύματος ώστε να γίνει η αποστολή του στον δέκτη
     for(int i=1;i<crc.length;i++){
         initial_message.add(crc[i]);
        // System.out.print(crc[i]);
     }


        // Δημιουργία "τυχαίων" αριθμών από το 0-100 , όταν ο αριθμός είναι <1 (BER =1/100) τότε αλλάζουμε το ψηφιό και υπάρχει λάθος .
     for(int i : initial_message){
         prob = Math.random()*100; // επιστρέφει "τυχαίο " αριθμό στο διάστημα 0-100
         if(prob<1) {
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


/**Μέσω της συνάρτησης main που τρέχουμε το πρόγραμμα , αρχικοποιούμε αρχικά το κλειδί pnumber σε 110101 όπως ορίζει η
 * άσκηση και συνέχεια μέσω μιας επανάληψης υλοποιούμε τον κύκλο : δημιουργία μηνύματος , εύρεση CRC , αποστολή και
 * παραλαβή μηνύματος και έλεγχος αποτελεσμάτων . Αφού τελειώσει η επανάληψη εμφανίζουμε τα κατάλληλα ποσοστά. **/

    public static void main (String args []) {

        pnumber[0]=1;
        pnumber[1]=1;
        pnumber[2]=0;
        pnumber[3]=1;
        pnumber[4]=0;
        pnumber[5]=1;
        Crc obj1 = new Crc();
        for (int i = 0; i < number_of_messages; i++) {
            obj1.message_generator(10);
            obj1.division(message_send, false); // Μια κλήση από την πλευρά του πομπού
            obj1.send_message();
            obj1.division(message_received, true); // Μια κλήση από την πλευρά του δέκτη
            // Μετά το τέλος κάθε μηνύματος και πριν την αρχή του επόμενου "καθαρίζουμε" τους πίνακες για την εισαγωγή των καινούργιων δεδομένων
            initial_message.clear();
            message_send.clear();
            message_received.clear();


    }
        //Εμφάνιση των στατιστικών
        System.out.println("Number of messages : " + number_of_messages);
        System.out.println("Messages with error : " + (messages_with_error/number_of_messages)*100  + "%" );
        System.out.println("Messages with error detected : " + (messages_with_error_detected_by_crc/number_of_messages)*100 + "%");
        System.out.println("Messages with error not detected : " + (messages_with_error_not_detected_by_crc/number_of_messages)+ "%");

    }
}
