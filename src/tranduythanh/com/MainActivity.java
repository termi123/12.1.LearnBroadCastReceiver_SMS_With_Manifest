package tranduythanh.com;
import android.os.Bundle;
import android.widget.Toast;
import android.app.Activity;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Toast.makeText(this, "Đã đăng ký background cho ứng dụng", Toast.LENGTH_LONG).show();
	}
}
