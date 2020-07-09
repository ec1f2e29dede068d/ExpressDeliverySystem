package c.w.g.bean;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "`order`")
public class Order implements Serializable {
    @Id
    private long id;
    private String state;
    private String date;
    private Integer receiveDelivererId;
    private Integer dispatchDelivererId;
    private Integer senderId;
    private String senderName;
    private String senderPhone;
    private String senderAddress;
    private String receiverName;
    private String receiverPhone;
    private String receiverAddress;
    private String type;
    private String weight;
    private String enterDate;
    private String outDate;
    private String receiveArea;
    private String dispatchArea;
    private String ps;
    private Integer dispatchErrorNum;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Integer getReceiveDelivererId() {
        return receiveDelivererId;
    }

    public void setReceiveDelivererId(Integer receiveDelivererId) {
        this.receiveDelivererId = receiveDelivererId;
    }

    public Integer getSenderId() {
        return senderId;
    }

    public void setSenderId(Integer senderId) {
        this.senderId = senderId;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderPhone() {
        return senderPhone;
    }

    public void setSenderPhone(String senderPhone) {
        this.senderPhone = senderPhone;
    }

    public String getSenderAddress() {
        return senderAddress;
    }

    public void setSenderAddress(String senderAddress) {
        this.senderAddress = senderAddress;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverPhone() {
        return receiverPhone;
    }

    public void setReceiverPhone(String receiverPhone) {
        this.receiverPhone = receiverPhone;
    }

    public String getReceiverAddress() {
        return receiverAddress;
    }

    public void setReceiverAddress(String receiverAddress) {
        this.receiverAddress = receiverAddress;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getEnterDate() {
        return enterDate;
    }

    public void setEnterDate(String enterDate) {
        this.enterDate = enterDate;
    }

    public String getOutDate() {
        return outDate;
    }

    public void setOutDate(String outDate) {
        this.outDate = outDate;
    }

    public String getReceiveArea() {
        return receiveArea;
    }

    public void setReceiveArea(String receiveArea) {
        this.receiveArea = receiveArea;
    }

    public Integer getDispatchDelivererId() {
        return dispatchDelivererId;
    }

    public void setDispatchDelivererId(Integer dispatchDelivererId) {
        this.dispatchDelivererId = dispatchDelivererId;
    }

    public String getDispatchArea() {
        return dispatchArea;
    }

    public void setDispatchArea(String dispatchArea) {
        this.dispatchArea = dispatchArea;
    }

    public String getPs() {
        return ps;
    }

    public void setPs(String ps) {
        this.ps = ps;
    }

    public Integer getDispatchErrorNum() {
        return dispatchErrorNum;
    }

    public void setDispatchErrorNum(Integer dispatchErrorNum) {
        this.dispatchErrorNum = dispatchErrorNum;
    }
}
