import javax.sound.midi.*;

public class MiniMusicPlayer2 implements ControllerEventListener {

    public static void main(String[] args) {
        MiniMusicPlayer2 mini = new MiniMusicPlayer2();
        mini.go();
    }



    public static MidiEvent makeEvent(int comd, int chan, int one, int two, int tick) {
        // First four params are for the Message, last is for when the message should be.
        MidiEvent event = null;
        try {
            ShortMessage a = new ShortMessage();
            a.setMessage(comd, chan, one, two);
            event = new MidiEvent(a, tick);
        } catch (Exception e) { }
        return event;
    }



    class MyDrawPanel extends JPanel implements ControllerEventListener {

        boolean msg = false;

        public void go() {
            try {
                // Make and open a sequencer.
                Sequencer sequencer = MidiSystem.getSequencer();
                sequencer.open();

                // Register for events. 127 is the event.
                int[] eventsIWant = {127};
                sequencer.addControllerEventListener(this, eventsIWant);

                Sequence seq = new Sequence(Sequence.PPQ, 4);
                Track track = seq.createTrack();

                for (int i = 5; i < 61; i +=4) {
                    track.add(makeEvent(144, 1, i, 100, i));

                    // How we pick up the beat. Event does nothing, we do this
                    // so we can pick it up when it fires.
                    track.add(makeEvent(176,1,127,0,i));
                    track.add(makeEvent(128, 1, i, 100, i + 2));
                }

                sequencer.setSequence(seq);
                sequencer.setTempoInBPM(220);
                sequencer.start();
            } catch (Exception ex) {ex.printStackTrace();}
        }

        public void controlChange(ShortMessage event) {
            msg = true;
            repaint();
        }

        public void paintComponent(Graphics g) {
            if (!msg) {
                return
            }

            Graphics2D g2 = (Graphics2D) g;
            int red = (int) (Math.random()) * 250);
            int green = (int) (Math.random()) * 250);
            int blue = (int) (Math.random()) * 250);
            g.setColor(new Color(red, green, blue));

            int height = (int) ((Math.random() * 120) + 10);
            int width = (int) ((Math.random() * 120) + 10);
            int x = (int) ((Math.random() * 40) + 10);
            int y = (int) ((Math.random() * 40) + 10);
            g.fillRect(x, y, height, width);
            msg = false;
        }
}
