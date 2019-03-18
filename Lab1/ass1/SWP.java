/*===============================================================*
 *  File: SWP.java                                               *
 *                                                               *
 *  This class implements the sliding window protocol            *
 *  Used by VMach class					         *
 *  Uses the following classes: SWE, Packet, PFrame, PEvent,     *
 *                                                               *
 *  Author: Professor SUN Chengzheng                             *
 *          School of Computer Engineering                       *
 *          Nanyang Technological University                     *
 *          Singapore 639798                                     *
 *===============================================================*/

import java.util.Timer;
import java.util.TimerTask;

public class SWP {

/*========================================================================*
 the following are provided, do not change them!!
 *========================================================================*/
   //the following are protocol constants.
   public static final int MAX_SEQ = 7;
   public static final int NR_BUFS = (MAX_SEQ + 1)/2; /* should be 2ˆn − 1 for selective repeat protocol*/

   // the following are protocol variables
   private int oldest_frame = 0; /* initial value is only for the simulator */
   private PEvent event = new PEvent();
   private Packet out_buf[] = new Packet[NR_BUFS];

   //the following are used for simulation purpose only
   private SWE swe = null;
   private String sid = null;

   //Constructor
   public SWP(SWE sw, String s){
      swe = sw;
      sid = s;
   }

   //the following methods are all protocol related

   //create and initialise our out buffer
   private void init(){
      for (int i = 0; i < NR_BUFS; i++){
	   out_buf[i] = new Packet();
      }
   }

   private void wait_for_event(PEvent e){
      swe.wait_for_event(e); //may be blocked
      oldest_frame = e.seq;  //set timeout frame seq
   }

   private void enable_network_layer(int nr_of_bufs) {
   //network layer is permitted to send if credit is available
      swe.grant_credit(nr_of_bufs);
   }

   private void from_network_layer(Packet p) {
      swe.from_network_layer(p);
   }

   private void to_network_layer(Packet packet) {
	    swe.to_network_layer(packet);
    }

   private void to_physical_layer(PFrame fm)  {
      System.out.println("SWP: Sending frame: seq = " + fm.seq +
			    " ack = " + fm.ack + " kind = " +
			    PFrame.KIND[fm.kind] + " info = " + fm.info.data );
      System.out.flush();
      swe.to_physical_layer(fm);
   }

  //get physcial frame from the physcial layer
  private void from_physical_layer(PFrame fm) {
      PFrame fm1 = swe.from_physical_layer();
	    fm.kind = fm1.kind;
	    fm.seq = fm1.seq;
	    fm.ack = fm1.ack;
	    fm.info = fm1.info;
   }


/*===========================================================================*
 	implement your Protocol Variables and Methods below:
 *==========================================================================*/

   boolean no_nak = true; /* no nak has been sent yet */

   //Timer initialization
   Timer[] f_timer = new Timer[NR_BUFS];
   Timer ack_timer = null;

  //make sure the frame to send does not exceed MAX_SEQ.
	public int inc(int next_frame_to_send) {
		return ((next_frame_to_send + 1) % (MAX_SEQ + 1));
	}

  //choose frame
	public boolean between(int frame_expected, int frame_seq, int too_far){
		return ((frame_expected <= frame_seq)&&(frame_seq < too_far))
				|| ((too_far < frame_expected)&&(frame_expected<=frame_seq))
				|| ((frame_seq<too_far)&&(too_far<frame_expected));
	}
  /* Construct and send a data, ack, or nak frame. */
   public void send_frame(int fk, int frame_nr, int frame_expected, Packet[] buffer){

	   PFrame fm = new PFrame();
	   fm.kind = fk;/* kind == data, ack, or nak */

     //if physcial frame is the correct type,
	   if(fm.kind == PFrame.DATA){
       fm.info = buffer[frame_nr%NR_BUFS];
     }

	   fm.seq = frame_nr; /* only meaningful for data frames */
	   fm.ack = (frame_expected + MAX_SEQ)%(MAX_SEQ+1);

	   if(fm.kind == PFrame.NAK) /* one nak per frame, please */
      no_nak = false;

	   to_physical_layer(fm); /* transmit the frame */

	   if(fm.kind == PFrame.DATA)
      start_timer(frame_nr);

	   stop_ack_timer(); // no need for separate ack frame, since piggybacking
   }

  public void protocol6() {

		int ack_expected = 0; /* lower edge of sender’s window , initialize to 0*/
		int next_frame_to_send = 0; // upper edge of sender’s window + 1 , next ack expected on the inbound stream is 0
		int frame_expected = 0; /* lower edge of receiver’s window */
		int too_far = NR_BUFS; /* upper edge of receiver’s window + 1 */
    int i; // index into buffer pool
		PFrame r = new PFrame(); /* scratch variable */
		Packet in_buf[] = new Packet[NR_BUFS]; /* buffers for the inbound stream */
    init(); /* buffers for the outbound stream */
		boolean[] arrived = new boolean[NR_BUFS];
    int nbuffered = 0; // initiall no packets buffered


    enable_network_layer(NR_BUFS); //initialize

		while(true) {
	         wait_for_event(event); // five possibilities:
		    switch(event.type) {

		      case (PEvent.NETWORK_LAYER_READY): { // accept,save, and transmit new frame.
		    	  from_network_layer(out_buf[next_frame_to_send%NR_BUFS]); //fetch new packet
		    	  send_frame(PFrame.DATA, next_frame_to_send, frame_expected, out_buf); //transmit frame
		    	  next_frame_to_send = inc(next_frame_to_send); //advance  upper window edge
            //nbuffered++; // expand the window
		      } break;

		      case (PEvent.FRAME_ARRIVAL ): {
            /* a data or control frame has arrived */
		    	  from_physical_layer(r);/* fetch incoming frame from physical layer */
		    	  if (r.kind == PFrame.DATA) { //if frame is undamaged and data is same
		    		  if ((r.seq != frame_expected) && no_nak)
		    			  send_frame(PFrame.NAK, 0, frame_expected, out_buf);
		    		  else
		    			  start_ack_timer();
		    		  if (between(frame_expected, r.seq, too_far) && (arrived[r.seq % NR_BUFS]==false)){
                /* Frames may be accepted in any order. */
    							arrived[r.seq % NR_BUFS] = true; /* mark buffer as full */
    							in_buf[r.seq  % NR_BUFS] = r.info; /* insert data into buffer */
							while(arrived[frame_expected % NR_BUFS]){
                /* Pass frames and advance window. */
								to_network_layer(in_buf[frame_expected % NR_BUFS]);
								no_nak = true;
								arrived[frame_expected % NR_BUFS] = false;
								frame_expected = inc(frame_expected); //advance lower edge of receiver’s window
								too_far = inc(too_far); /* advance upper edge of receiver’s window */
								start_ack_timer(); /* to see if a separate ack is needed */
							}
						}
		    	  }
            //resend frame if NAK recieved
		    	  if((r.kind == PFrame.NAK) && between(ack_expected, (r.ack + 1)%(MAX_SEQ + 1),next_frame_to_send))
						      send_frame(PFrame.DATA, (r.ack+1)%(MAX_SEQ+1), frame_expected, out_buf);

					while(between(ack_expected, r.ack, next_frame_to_send)){
					//	nbuffered--; /* handle piggybacked ack */
						stop_timer(ack_expected % NR_BUFS); /* frame arrived intact */
						ack_expected = inc(ack_expected);  /* advance lower edge of sender’s window */
						enable_network_layer(1); // 1 buffer enabled
					}
		      } break;

		      case (PEvent.CKSUM_ERR): {
		    	  if(no_nak)
		    		  send_frame(PFrame.NAK, 0, frame_expected, out_buf); //damaged frame, send a NAK to the other machine
		      } break;

		      case (PEvent.TIMEOUT): {
		    	  send_frame(PFrame.DATA, oldest_frame, frame_expected, out_buf); //timed out, resend again
		      } break;

		      case (PEvent.ACK_TIMEOUT): {
		    	  send_frame(PFrame.ACK, 0, frame_expected, out_buf); /* ack timer expired; send ack */
		      }
		    	  break;

		      default:
			   System.out.println("SWP: undefined event type = " + event.type);
			   System.out.flush();
		    }
        /*if (nbuffered < NR_BUFS) {
         enable_network_layer(nbuffered%NR_BUFS);
        }
        */
		}
  }

 /* Note: when start_timer() and stop_timer() are called,
    the "seq" parameter must be the sequence number, rather
    than the index of the timer array,
    of the frame associated with this timer,
   */

   private void start_timer(int seq) {
	   stop_timer(seq);
       //create new timer and new timertask
       f_timer[seq % NR_BUFS] = new Timer();
       //schedule the  task for execution after 200ms
       f_timer[seq % NR_BUFS].schedule(new f_task(seq), 200);
   }

   private void stop_timer(int seq) {
	   if (f_timer[seq % NR_BUFS] != null) {
           f_timer[seq % NR_BUFS].cancel();
           f_timer[seq % NR_BUFS] = null;
       }
   }

   private void start_ack_timer() {
	   stop_ack_timer();

       //starts another timer for sending separate ack
       ack_timer = new Timer();
       ack_timer.schedule(new ack_task(), 50);
   }

   private void stop_ack_timer() {
	   if (ack_timer != null) {
           ack_timer.cancel();
           ack_timer = null;
       }
   }

   class ack_task extends TimerTask {

        public void run() {
            //stop timer
            stop_ack_timer();
            swe.generate_acktimeout_event();
        }
    }

   class f_task extends TimerTask {

        private int seq;

        public f_task(int seq) {
            this.seq = seq;
        }

        public void run() {
            //stops this timer, discarding any scheduled tasks for the current seq
            stop_timer(seq);
            swe.generate_timeout_event(seq);
        }
    }
}//End of class


/* Note: In class SWE, the following two public methods are available:
   . generate_acktimeout_event() and
   . generate_timeout_event(seqnr).

   To call these two methods (for implementing timers),
   the "swe" object should be referred as follows:
     swe.generate_acktimeout_event(), or
     swe.generate_timeout_event(seqnr).
*/
