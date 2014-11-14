package tranduythanh.com;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.widget.Toast;

public class MyBroadCast extends BroadcastReceiver {

	// All available column names in SMS table
	// [_id, thread_id, address, 
	// person, date, protocol, read, 
	// status, type, reply_path_present, 
	// subject, body, service_center, 
	// locked, error_code, seen]
	public static final String SMS_EXTRA_NAME = "pdus";
	public static final String SMS_URI = "content://sms";

	public static final String ADDRESS = "address";
	public static final String PERSON = "person";
	public static final String DATE = "date";
	public static final String READ = "read";
	public static final String STATUS = "status";
	public static final String TYPE = "type";
	public static final String BODY = "body";
	public static final String SEEN = "seen";

	public static final int MESSAGE_TYPE_INBOX = 1;
	public static final int MESSAGE_TYPE_SENT = 2;

	public static final int MESSAGE_IS_NOT_READ = 0;
	public static final int MESSAGE_IS_READ = 1;

	public static final int MESSAGE_IS_NOT_SEEN = 0;
	public static final int MESSAGE_IS_SEEN = 1;
	private Context context=null;
	@Override
	public void onReceive(Context context, Intent intent) {
		this.context=context;
		processSMS(intent);
	}
	public void processSMS(Intent intent)
	{
		// Get the SMS map from Intent
		Bundle extras = intent.getExtras();

		String messages = "";

		if ( extras != null )
		{
			// Get received SMS array
			Object[] smsExtra = (Object[]) extras.get( SMS_EXTRA_NAME );
			for ( int i = 0; i < smsExtra.length; ++i )
			{
				SmsMessage sms = SmsMessage.createFromPdu((byte[])smsExtra[i]);
				String body = sms.getMessageBody().toString();
				String address = sms.getOriginatingAddress();

				messages += "SMS from " + address + " :\n";                    
				messages += body + "\n";
				//gọi hàm xử lý nội dung và gửi kết quả trả về
				doSending(address,body);
			}
			// Display SMS message từ các điện thoại khác gửi tới
			Toast.makeText( context, messages, Toast.LENGTH_SHORT ).show();
		}
	}
	public void doSending(String phoneNumber,String body)
	{
		try
		{
			//đặng ký Pending Intent để kiểm soát kết quả gửi tin nhắn trả về thành công hay không?
			final SmsManager sms = SmsManager.getDefault();
			Intent msgSent = new Intent("ACTION_MSG_SENT");
			final PendingIntent pendingMsgSent =
					PendingIntent.getBroadcast(context, 0, msgSent, 0);
			context.getApplicationContext().registerReceiver(new BroadcastReceiver() {
				public void onReceive(Context context, Intent intent) {
					int result = getResultCode();
					String msg="Gửi tin nhắn trả lời thành công";
					if (result != Activity.RESULT_OK) {
						msg="Gửi tin nhắn trả lời thất bại";
					} 
					Toast.makeText(context, msg, 
							Toast.LENGTH_LONG).show();
				}
			}, new IntentFilter("ACTION_MSG_SENT"));
			//nếu đúng là cú pháp đăng ký học phần.
			if(isSyntaxForRegisterCourse(body))
			{
				processRegisterCourse(body);
				sms.sendTextMessage(phoneNumber, null, "Hệ thống đã nhận được tin đăng ký học phần của bạn! chờ giây lát!", 
						pendingMsgSent, null);
			}
			else
			{
				//chỉ là tin nhắn thường
				//tự động trả lời -- coi chừng tốn tiền khi dùng máy thật nhá
				sms.sendTextMessage(phoneNumber, null, "DrThanh tự động trả lời bạn! Củm ơn!", 
						pendingMsgSent, null);	
			}
			
		}
		catch(Exception ex)
		{
			Toast.makeText(context, ex.getMessage(),Toast.LENGTH_LONG).show();
		}
	}
	//Giả sử cú pháp bạn quy định là:
	//DRTHANH MSSV MMH HK
	//DRTHANH là keyword đầu tiên của tin nhắn
	//MSSV là mã số sinh viên
	//MMH là mã môn học
	//HK là học kỳ
	
	//VÍ DỤ body= DRTHANH 113114115 ANDROID 9
	//Thì mã sinh viên =113114115
	//mã môn học: ANDROID
	//học kỳ 9
	public boolean isSyntaxForRegisterCourse(String body)
	{
		String arr[]=body.split(" ");
		if(arr.length!=4)
			return false;
		if(!arr[0].equalsIgnoreCase("DRTHANH"))
			return false;
		return true;
	}
	/**
	 * Các bạn xử lý đăng ký học phần tại đây
	 * Phân tích và gửi dữ liệu lên Server thông qua Webservice
	 * Vậy trên Server bạn viết 1 Webservice xử lý đăng ký học phần là OKÊ
	 * @param body
	 */
	public void processRegisterCourse(String body)
	{
		//tự viết lệnh chỗ này
		//xem lại các bài tập mà Tui hướng dẫn trước đó để làm
		//chúc bạn may mắn.
	}
}
