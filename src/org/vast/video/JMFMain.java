package org.vast.video;

import com.sun.media.util.ByteBuffer;
import javax.media.*;
import javax.media.format.*;
import javax.media.control.*;
import com.sun.media.codec.video.jpeg.NativeDecoder;


public class JMFMain implements ControllerListener{
	Processor p;
	int[] waitSync = new int[0];
	boolean stateTransOK = true;
	Buffer buf=null;
	FrameGrabbingControl frameGrabber;
    FramePositioningControl framePositioner;
	int theData[];
	int dataLength;
	int dataOffset;
	int imageScanSkip=5;
	int size=128;//64;
	boolean first=true;
    int currentFrame = 0;
    
    
	public JMFMain(){
		
	}
    public boolean open(MediaLocator ml){
    	try {
           p = Manager.createProcessor(ml);
    	} catch (Exception e) {
    	    System.err.println("Failed to create a processor: " + e);
    	    return false;
    	}

    	p.addControllerListener(this);

    	// Put the Processor into configured state.
    	p.configure();
    	if (!waitForState(Processor.Configured)) {
    	    System.err.println("Failed to configure the processor.");
    	    return false;
    	}

    	// So I can use it as a player.
        //p.setContentDescriptor(new FileTypeDescriptor(FileTypeDescriptor.MPEG));
    	p.setContentDescriptor(null);

    	// Obtain the track controls.
    	TrackControl tc[] = p.getTrackControls();
        
    	if (tc == null) {
    	    System.err.println("Failed to obtain track controls from the processor.");
    	    return false;
    	}

    	// Search for the track control for the video track.
    	TrackControl videoTrack = null;

    	for (int i = 0; i < tc.length; i++) {
    	    if (tc[i].getFormat() instanceof VideoFormat) {
    		videoTrack = tc[i];
            
            try
            {
                Codec codec = new NativeDecoder();
                Format[] formats = codec.getSupportedOutputFormats(videoTrack.getFormat());
                //Vector plugins = PlugInManager.getPlugInList(videoTrack.getFormat(), new RGBFormat(), PlugInManager.CODEC);
                codec.setInputFormat(videoTrack.getFormat());
                codec.setOutputFormat(formats[0]);
                codec.open();
                videoTrack.setCodecChain(new Codec[] {codec});
            }
            catch (Exception e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

    		//break;
    		
    	    }
            else if (tc[i].getFormat() instanceof AudioFormat)
            {
                tc[i].setEnabled(false);
            }
    	}

    	if (videoTrack == null) {
    	    System.err.println("The input media does not contain a video track.");
    	    return false;
    	}

    	System.err.println("Video format: " + videoTrack.getFormat());
    	        
    	p.prefetch();       
    	if(!waitForState(Controller.Prefetched)){
    		System.out.println("failed to prefetch the processor");
    		return false;
    	}
    	System.out.println("end of prefetch");
    	return true;
    }
    public void init(){
       	
    	try{
    		frameGrabber = (FrameGrabbingControl)p.getControl("javax.media.control.FrameGrabbingControl");
    	    framePositioner = (FramePositioningControl)p.getControl("javax.media.control.FramePositioningControl");
        
    	    framePositioner.seek(0);
    	    while(buf==null){
    	    	buf = frameGrabber.grabFrame();
    	    }
    	    dataLength = buf.getLength();
    	    dataOffset = buf.getOffset();
    	    dataLength = dataLength-dataOffset;
    	}catch(Exception e){
    		System.err.println("init of JMFMain haulted: " + e);
    	}
    	finally{
    		System.out.println("Everything is running smoothly");
    	}
    }
    public boolean waitForState(int state){
		synchronized(waitSync){
    		try{
    			while(p.getState()!=state&&stateTransOK){
    				waitSync.wait();		
    			}
    		}catch(Exception ex){}
    		return stateTransOK;
    	}
    }
    public boolean isEndOfMedia(){
    	long startTime = p.getMediaTime().getNanoseconds();
        Time duration = p.getDuration();
        float rate = p.getRate();
                
        if ((startTime > duration.getNanoseconds() && rate >= 0.0f) || 
            (startTime < 0 && rate <= 0.0f)) 
        {
            return true;
        } 
        
        else 
        {
            return false;
        }
    }
    public Buffer getFrame(int frameCount){
    	//if(frameCount%100!=0)return null;
    	Buffer b = buf;
    	/*int[] rawData = (int[]) buf.getData();
    	if (rawData == null)
            return null;
        
    	int totalSize = size*size*3;
    	byte[] texData = new byte[totalSize];
        for(int i = 0 ; i < texData.length ; i+=3)
        {
        	// lines are given from bottom to top apparently !
            int j = 19199 - 160 - i/(size*3) * 160 + i%(size*3)/3;
            if (j >= 0)
            {
                texData[i] = (byte)(rawData[j] >> 16 & 0xFF);
                texData[i+1] = (byte)(rawData[j] >> 8 & 0xFF);
                texData[i+2] = (byte)(rawData[j] & 0xFF);
            }
            else
            {
                texData[i] = 0;
                texData[i+1] = 0;
                texData[i+2] = 0;
            }
        }   */	
    
        	
        if(dataLength==0){System.exit(0);}
        
        currentFrame++;
        if (currentFrame > 100)
            currentFrame = 0;
        framePositioner.seek(currentFrame);
    	buf = frameGrabber.grabFrame();
        return b; 
    }
	public void controllerUpdate(ControllerEvent evt) {
		if( evt instanceof ConfigureCompleteEvent ||
			evt instanceof RealizeCompleteEvent ||
			evt instanceof PrefetchCompleteEvent ){
			synchronized (waitSync) {
				stateTransOK = true;
				waitSync.notifyAll();
			}
		}
		else if( evt instanceof ResourceUnavailableEvent) {
			synchronized (waitSync) {
				stateTransOK = false;
				waitSync.notifyAll();
			}
		}
	}
	public class PreAccessCodec implements Codec {

		/**
	         * Callback to access individual video frames.
	         */
		void accessFrame(Buffer frame) {

		    // For demo, we'll just print out the frame #, time &
		    // data length.

		    long t = (long)(frame.getTimeStamp()/10000000f);

		    System.err.println("Pre: frame #: " + frame.getSequenceNumber() + 
				", time: " + ((float)t)/100f + 
				", len: " + frame.getLength());
		}


		/**
	 	 * The code for a pass through codec.
		 */

		// We'll advertize as supporting all video formats.
		protected Format supportedIns[] = new Format [] {
		    new VideoFormat(null)
		};

		// We'll advertize as supporting all video formats.
		protected Format supportedOuts[] = new Format [] {
		    new VideoFormat(null)
		};

		Format input = null, output = null;

		public String getName() {
		    return "Pre-Access Codec";
		}

		// No op.
	        public void open() {
		}

		// No op.
		public void close() {
		}

		// No op.
		public void reset() {
		}

		public Format [] getSupportedInputFormats() {
		    return supportedIns;
		}

		public Format [] getSupportedOutputFormats(Format in) {
		    if (in == null)
			return supportedOuts;
		    else {
			// If an input format is given, we use that input format
			// as the output since we are not modifying the bit stream
			// at all.
			Format outs[] = new Format[1];
			outs[0] = in;
			return outs;
		    }
		}

		public Format setInputFormat(Format format) {
		    input = format;
		    return input;
		}

		public Format setOutputFormat(Format format) {
		    output = format;
		    return output;
		}

		public int process(Buffer in, Buffer out) {

		    // This is the "Callback" to access individual frames.
		    accessFrame(in);

		    // Swap the data between the input & output.
		    Object data = in.getData();
		    in.setData(out.getData());
		    out.setData(data);

		    // Copy the input attributes to the output
		    out.setFormat(in.getFormat());
		    out.setLength(in.getLength());
		    out.setOffset(in.getOffset());

		    return BUFFER_PROCESSED_OK;
		}

		public Object[] getControls() {
		    return new Object[0];
		}

		public Object getControl(String type) {
		    return null;
		}
	    }

	    public class PostAccessCodec extends PreAccessCodec {
		// We'll advertize as supporting all video formats.
		public PostAccessCodec() {
		    supportedIns = new Format [] {
			new RGBFormat()
		    };
		}

		/**
	         * Callback to access individual video frames.
	         */
		void accessFrame(Buffer frame) {

		    // For demo, we'll just print out the frame #, time &
		    // data length.

		    long t = (long)(frame.getTimeStamp()/10000000f);

		    System.err.println("Post: frame #: " + frame.getSequenceNumber() + 
				", time: " + ((float)t)/100f + 
				", len: " + frame.getLength());
		}

		public String getName() {
		    return "Post-Access Codec";
		}
	    }
}
