package refusednat;

import java.net.InetSocketAddress;

public class NatResult {

    private boolean drop = true;
    private boolean sendToLan = false;
    private boolean sendToWan = false;
    private boolean changeTarget = false;
    private boolean changeSource = false;
    private InetSocketAddress target = null;
    private InetSocketAddress source = null;

    public boolean isDrop() {
        return drop;
    }

    public void setDrop(boolean drop) {
        this.drop = drop;
    }

    public boolean isSendToLan() {
        return sendToLan;
    }

    public void setSendToLan(boolean sendToLan) {
        this.sendToLan = sendToLan;
    }

    public boolean isSendToWan() {
        return sendToWan;
    }

    public void setSendToWan(boolean sendToWan) {
        this.sendToWan = sendToWan;
    }

    public boolean isChangeTarget() {
        return changeTarget;
    }

    public void setChangeTarget(boolean changeTarget) {
        this.changeTarget = changeTarget;
    }

    public boolean isChangeSource() {
        return changeSource;
    }

    public void setChangeSource(boolean changeSource) {
        this.changeSource = changeSource;
    }

    public InetSocketAddress getTarget() {
        return target;
    }

    public void setTarget(InetSocketAddress target) {
        this.target = target;
    }

    public InetSocketAddress getSource() {
        return source;
    }

    public void setSource(InetSocketAddress source) {
        this.source = source;
    }
}
