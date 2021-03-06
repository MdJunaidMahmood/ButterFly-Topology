public class Processor{
  private int processorID;
  int rank;
  
  private ProcessorToSwitchLink outgoing;
  private SwitchToProcessorLink incoming;

  public Processor(int id, int rank){
    this.processorID = id;
    this.rank = rank;
  }

  public void addProcessorToSwitchLink(ProcessorToSwitchLink cable){
    this.outgoing = cable;
  }

  public void addSwitchToProcessorLink(SwitchToProcessorLink cable){
    this.incoming = cable;
  }

  public String toString(){
    return "Pr  " + this.processorID;
  }

  public void send(int receiver, Packet p){
    System.out.print("Sender\'s Processor: ");
    System.out.println(this.toString());
   

    String recv = new String("");
    for(int i = 1; i < this.rank; i++){
      recv = recv + "0";
    }

    int pos = rank - 2;
    boolean flag = true;
    while(receiver != 0){
      int value = receiver % 2;
      char insert = (char)(value + 48);
      
      if(flag){
        recv = recv.substring(0, pos) +
               Character.toString(insert);
        flag = false;
      }else{
        recv = recv.substring(0, pos) +
               Character.toString(insert) +
               recv.substring(pos + 1);
      }
      pos--;
      receiver = (int)(receiver / 2);
    }

    recv = recv + "X";

    
 //   System.out.println("\t" + recv);
    outgoing.transmit(recv, p);
  }

  public void receive(Packet p){
    System.out.print("Receiver\'s Processor: ");
    System.out.println(this.toString() + " ");
    String message = p.msg;
    System.out.print("Message Received: ");
    System.out.println(message);
    System.out.println("\n");
  }

  public void broadcast(int num, String message){
    System.out.println("Starting Broadcast\n");
    
    String array[] = new String[num];
    
    for(int i = 0; i < num; i++){
      if(i == this.processorID){
        array[i] = null;
      }else{
        String recv = new String("");
        for(int j = 1; j < this.rank; j++){
          recv = recv + "0";
        }

        int pos = rank - 2;
        boolean flag = true;
        int receiver = i;
        while(receiver != 0){
          int value = receiver % 2;
          char insert = (char)(value + 48);
          
          if(flag){
            recv = recv.substring(0, pos) +
                  Character.toString(insert);
            flag = false;
          }else{
            recv = recv.substring(0, pos) +
                  Character.toString(insert) +
                  recv.substring(pos + 1);
          }
          pos--;
          receiver = (int)(receiver / 2);
        }

        recv = recv + "X";
        array[i] = recv;
      }
    }

    Packet p = new Packet(message, this.processorID, true, array);
    this.outgoing.transmit(p);
      
    System.out.println("");
    
  }
}