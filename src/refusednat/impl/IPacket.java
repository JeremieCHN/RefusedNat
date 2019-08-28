package refusednat.impl;

import java.net.InetSocketAddress;

public interface IPacket {
    public boolean isRecvFromLan();
    public boolean isRecvFromWan();
    public InetSocketAddress getSourceAddress();
    public InetSocketAddress getTargetAddress();
}
