import com.cloudstore.dev.api.bean.MessageBean;
import com.cloudstore.dev.api.bean.MessageType;
import com.cloudstore.dev.api.util.Util_Message;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

//E9二开、第三方系统通过调用java源码推送消息
public class MessageApi {

    //发送消息：
    void sendMessage()
    {
        MessageType messageType = MessageType.newInstance(121); // 消息来源（见文档第四点补充 必填）
        Set<String> userIdList = new HashSet<>(); // 接收人id 必填
        String title = "标题"; // 标题
        String context = "内容"; // 内容
        String linkUrl = "PC端链接"; // PC端链接
        String linkMobileUrl = "移动端链接"; // 移动端链接

        try {
            MessageBean messageBean = Util_Message.createMessage(messageType, userIdList, title, context, linkUrl, linkMobileUrl);
            messageBean.setCreater(1);// 创建人id
            //message.setBizState("0");// 需要修改消息为已处理等状态时传入,表示消息最初状态为待处理
            //messageBean.setTargetId("121|22"); //消息来源code +“|”+业务id需要修改消息为已处理等状态时传入
            Util_Message.store(messageBean);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    修改消息业务状态：
    void updateMessage()
    {
        try {
            MessageBean messageBean = Util_Message.createMessage();
            messageBean.setUserList(new HashSet<>());//接收人id
            messageBean.setTargetId("121|22"); //targetId code +“|”+业务id删除消息时所依据的条件
            messageBean.setBizState("bizState"); //bizState 业务状态 待处理 0 已处理 1 已同意 2 已拒绝 3 已删除 27 已暂停 34 已撤销 35
            //messageBean.setMessageType(MessageType.newInstance(121));//消息来源code(传了代表code也做为修改时的条件，默认不传）
            Util_Message.updateBizState(messageBean);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


//    删除消息：
    void deleteMessage()
    {
        try {
            MessageBean messageBean = Util_Message.createMessage();
            messageBean.setUserList(new HashSet<>());//接收人id
            messageBean.setTargetId("121|22"); //code + “|” + 业务id
            //messageBean.setMessageType(MessageType.newInstance(121));//消息来源code(传了代表code也做为删除时的条件，默认不传）
            Util_Message.delMessageTargetid(messageBean);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public static void main(String[] args) {

    }




}

