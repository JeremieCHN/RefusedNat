package refusednat.impl;

/**
 * The Interface of port manager, as I think, a port manager should
 * be used to get / recovery a port, and time limit is useful, so
 * this interface contains the functions
 */
public interface IPortManager {
    /**
     * get a new port, and its overtime is the default
     *
     * @return port
     */
    public int get();

    /**
     * recover the port
     *
     * @param port the port
     */
    public void recover(int port);

    /**
     * Extend the overtime of the port given
     *
     * @param port the port
     */
    public void renewalPort(int port);

    /**
     * Set the overtime of new ports
     *
     * @param time millisecond
     */
    public void setConfigTime(long time);

    /**
     * Start the timer
     */
    public void startTimer();

    /**
     * Stop the timer
     */
    public void stopTimer();


    public interface OvertimeListener {
        public void onOvertime(int port);
    }
}