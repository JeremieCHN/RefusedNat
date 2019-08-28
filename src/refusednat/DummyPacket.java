package refusednat;

import refusednat.impl.IPacket;

import java.net.InetSocketAddress;

public class DummyPacket implements IPacket {
    private boolean recvFromLan = false;
    private boolean recvFromWan = false;
    private InetSocketAddress source = null;
    private InetSocketAddress target = null;

    public void setRecvFrom(boolean isLan) {
        recvFromLan = isLan;
        recvFromWan = !isLan;
    }

    public void setSource(InetSocketAddress addr) {
        source = addr;
    }

    public void setTarget(InetSocketAddress addr) {
        target = addr;
    }


    @Override
    public boolean isRecvFromLan() {
        return recvFromLan;
    }

    @Override
    public boolean isRecvFromWan() {
        return recvFromWan;
    }

    @Override
    public InetSocketAddress getSourceAddress() {
        return source;
    }

    @Override
    public InetSocketAddress getTargetAddress() {
        return target;
    }
}
