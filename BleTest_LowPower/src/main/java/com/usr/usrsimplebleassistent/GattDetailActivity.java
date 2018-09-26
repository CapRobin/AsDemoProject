package com.usr.usrsimplebleassistent;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.usr.usrsimplebleassistent.BlueToothLeService.BluetoothLeService;
import com.usr.usrsimplebleassistent.Utils.AnimateUtils;
import com.usr.usrsimplebleassistent.Utils.Constants;
import com.usr.usrsimplebleassistent.Utils.GattAttributes;
import com.usr.usrsimplebleassistent.Utils.Utils;
import com.usr.usrsimplebleassistent.adapter.MessagesAdapter;
import com.usr.usrsimplebleassistent.adapter.OptionsSelectAdapter;
import com.usr.usrsimplebleassistent.bean.Message;
import com.usr.usrsimplebleassistent.bean.Option;
import com.usr.usrsimplebleassistent.views.OptionsMenuManager;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.drakeet.materialdialog.MaterialDialog;

import static java.lang.Thread.sleep;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.widget.Toast;

public class GattDetailActivity extends MyBaseActivity {

    @Bind(R.id.btn_options)
    ImageButton btnOptions;
    @Bind(R.id.btn_option)
    Button btnOption;
    @Bind(R.id.lv_msg)
    RecyclerView rvMsg;
    @Bind(R.id.tv_properties)
    TextView tvProperties;
    @Bind(R.id.et_write)
    EditText etWrite;
    @Bind(R.id.btn_send)
    Button btnSend;
    @Bind(R.id.btn_ggpl)
    Button btnGgpl;
    @Bind(R.id.rl_write)
    RelativeLayout rlWrite;
    @Bind(R.id.rl_content)
    RelativeLayout rlContent;
    @Bind(R.id.rl_bottom)
    RelativeLayout rlBottom;
    @Bind(R.id.view_bottom_shadow)
    View bottomShadow;
    @Bind(R.id.view_top_shadow)
    View topShadow;
    @Bind(R.id.view_filter)
    View filterView;
    @Bind(R.id.btn_select)
    Button btnSelect;
    @Bind(R.id.lst_data)
    ListView listview;//数据显示
    @Bind(R.id.edit_addr)
    EditText editAddr;

    private final List<Message> list = new ArrayList<>();

    private MessagesAdapter adapter;

    private BluetoothGattCharacteristic notifyCharacteristic;
    private BluetoothGattCharacteristic readCharacteristic;
    private BluetoothGattCharacteristic writeCharacteristic;
    private BluetoothGattCharacteristic indicateCharacteristic;

    private MyApplication myApplication;
    private String properties;
    private OptionsMenuManager optionsMenuManager;

    private List<Option> options = new ArrayList<>();
    private Option currentOption;

    private boolean isHexSend = false;

    private boolean nofityEnable;
    private boolean indicateEnable;
    private boolean isDebugMode;
    private StringBuilder sb = new StringBuilder();//缓存接收数据
    private SimpleAdapter simpleAdapter;

//    private EditText recvEdit;

    /* class SendAndRecive implements Runnable {//发送接收线程
         private String text;
         private DltGas645 gas645;
         public SendAndRecive(String text) {
             this.text = text;

         }
         public void run() {
             writeOption(text);
             int bz = -1;
             for (int i =0; i<200; i++)
             {
                 try
                 {
                     sleep(10);
                 }
                 catch (Exception ex)
                 {
                     ex.printStackTrace();
                 }
                 bz = DltGas645.AnalyseData(sb.toString().trim());
                 if (bz ==0)
                     break;
             }

         }
     }
 */
    private ArrayList<String> MakeData(String str)//生成解析数据
    {
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.delete(0, 20);// 删除68...68
        // String meterType = sb.substring(0,2);//80是仪表类型
        sb.delete(0, 2);
        String controlCode = sb.substring(0, 2);//01是控制码
        sb.delete(0, 2);
        String dataLenth = sb.substring(0, 2);// 数据长度
        sb.delete(0, 2);
        String dataZone = sb.substring(0, Integer.parseInt(dataLenth, 16) * 2);// 数据域
        ArrayList<String> list = new ArrayList<String>();
        if (controlCode.equals("01"))//抄数据返回的命令
        {
            String cmd = dataZone.substring(0, 8);// 数据标识
            cmd = reverbyte(cmd);// 倒置
            String dataStr = dataZone.substring(8);// 数据
            switch (cmd) {
                case "71040001":
                    list.clear();
//                                            680C100000785634126880010A010004710000785634121516
                    list.add("通讯地址|" + reverbyte(dataStr.substring(0, 12)) + "|" + "");
                    break;
                case "0400010C":
                    try {
                        list.clear();
                        list.add("抄读气表时间|" + addSign(reverbyte(dataStr.substring(0, 14)), "YYMMDDWWhhmmss") + "|");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case "7000FF00":// 当前气量
                    try {
                        list.clear();
//                        String tempBug = sb.substring(90, 98);
//                        tempBug = addSign(reverbyte(tempBug.substring(0, 8)), "xxxxxx.xx");
//                        String temp = addSign(reverbyte(dataStr.substring(0, 8)), "xxxxxx.xx");
//                        double d = Double.parseDouble(temp) - Double.parseDouble(tempBug);
//                        String rightStr = String.format("%.2f", d);//当前结算周期气量不对
                        list.add("当前气量|" + addSign(reverbyte(dataStr.substring(0, 8)), "xxxxxx.xx") + "|" + "m³");
                        list.add("当前结算周期气量|" + addSign(reverbyte(dataStr.substring(8, 16)), "xxxxxx.xx") + "|" + "m³");
//                        list.add("当前结算周期气量|" + rightStr + "|" + "m³");
                        list.add("剩余金额（总）|" + addSign(reverbyte(dataStr.substring(16, 24)), "xxxxxx.xx") + "|" + "元");
                        list.add("剩余金额（本地）|" + addSign(reverbyte(dataStr.substring(24, 32)), "xxxxxx.xx") + "|" + "元");//保留
                        list.add("购气次数（本地）|" + addSign(reverbyte(dataStr.substring(32, 40)), "xxxxxxxx") + "|" + "次"); //保留
                        list.add("剩余金额（远程）|" + addSign(reverbyte(dataStr.substring(40, 48)), "xxxxxx.xx") + "|" + "元");
                        list.add("购气次数（远程）|" + addSign(reverbyte(dataStr.substring(48, 56)), "xxxxxxxx") + "|" + "次");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case "7200FF00":// 实时状态查询
                    list.clear();
                    list.add("电池电压|" + addSign(reverbyte(dataStr.substring(0, 4)), "xx.xx") + "|" + "V");
                    list.add("实时时间|" + addSign(reverbyte(dataStr.substring(4, 18)), "YYMMDDWWhhmmss") + "|" + " ");
                    String bitStr = reverbyte(dataStr.substring(18, 22));
                    String bit = AddZeros(Integer.toBinaryString(Integer.valueOf(bitStr, 16)), 16);
                    bit = reverbyte1(bit);
                    if (bit.charAt(0) == '1')
                        list.add("阀开/关|1:状态|开");
                    else
                        list.add("阀开/关|0:状态|关");
                    if (bit.charAt(1) == '1')
                        list.add("阀异常|1:状态|是");
                    else
                        list.add("阀异常|0:状态|否");
                    if (bit.charAt(2) == '1')
                        list.add("电压低|1:状态|是");
                    else
                        list.add("电压低|0:状态|否");
                    if (bit.charAt(3) == '1')
                        list.add("干簧管坏|1:状态|是");
                    else
                        list.add("干簧管坏|0:状态|否");
                    if (bit.charAt(4) == '1')
                        list.add("强磁干扰|1:状态|有");
                    else
                        list.add("强磁干扰|0:状态|无");
                    if (bit.charAt(5) == '1')
                        list.add("EEPROM异常|1:状态|是");
                    else
                        list.add("EEPROM异常|0:状态|否");
                    if (bit.charAt(6) == '1')
                        list.add("ESAM异常|1:状态|是");
                    else
                        list.add("ESAM异常|0:状态|否");
                    if (bit.charAt(7) == '1')
                        list.add("余额不足|1:状态|是");
                    else
                        list.add("余额不足|0:状态|否");
                    if (bit.charAt(8) == '1')
                        list.add("RTC异常|1:状态|是");
                    else
                        list.add("RTC异常|0:状态|否");
                    if (bit.charAt(9) == '1')
                        list.add("已开户|1:状态|是");
                    else
                        list.add("已开户|0:状态|否");
                    if (bit.charAt(10) == '1')
                        list.add("剩余金额小于最小扣费金额|1:状态|是");
                    else
                        list.add("剩余金额小于最小扣费金额|0:状态|否");
                    if (bit.charAt(11) == '1')
                        list.add("保留，卡充钱包剩余为0|1:状态|是");
                    else
                        list.add("保留，卡充钱包剩余为0|0:状态|否");
                    if (bit.charAt(12) == '1')
                        list.add("网充钱包剩余为0|1:状态|是");
                    else
                        list.add("网充钱包剩余为0|0:状态|否");
                    break;

                case "7002FF00": // 月冻结数据
                case "7002FF01":
                case "7002FF02":
                case "7002FF03":
                case "7002FF04":
                case "7002FF05":
                    list.clear();
                    int times = Integer.parseInt(cmd.substring(6, 8), 16);
                    String tipStr;
                    if (times == 0)
                        tipStr = "当前月";
                    else
                        tipStr = "上" + times + "月";
                    list.add(tipStr + "冻结累计用气量|" + addSign(reverbyte(dataStr.substring(0, 8)), "xxxxxx.xx") + "|" + "m³");
                    list.add(tipStr + "冻结累计使用金额|" + String.format("%.2f", Integer.parseInt(reverbyte(dataStr.substring(8, 16))) * 0.001) + "|" + "元");
                    list.add(tipStr + "冻结价格版本号|" + String.valueOf(Integer.parseInt(reverbyte(dataStr.substring(16, 20)), 16)) + "|" + "");
                    list.add("统计时间|" + addSign(reverbyte(dataStr.substring(20, 30)), "YYMMDDhhmm") + "|" + "");
                    //  case "70000001"://冻结总次数
                    //      list.clear();
                    //      list.add("冻结总次数|"+reverbyte(dataStr.substring(0, 4))+"|"+"次" );
                    break;
                case "7000FF01"://上1结算数据块
                case "7000FF02":
                case "7000FF03":
                case "7000FF04":
                case "7000FF05":
                case "7000FF06":
                case "7000FF07":
                case "7000FF08":
                case "7000FF09":
                    list.clear();
                    times = Integer.parseInt(cmd.substring(6, 8), 16);
                    tipStr = "上" + times + "结算";
                    list.add("总次数|" + String.valueOf(Integer.parseInt(reverbyte(dataStr.substring(0, 4)), 16)) + "|" + "次");
                    list.add(tipStr + "时间|" + addSign(reverbyte(dataStr.substring(4, 14)), "YYMMDDhhmm") + "|");
                    list.add(tipStr + "总气量|" + addSign(reverbyte(dataStr.substring(14, 22)), "xxxxxx.xx") + "|m3");
                    list.add(tipStr + "周期气量|" + addSign(reverbyte(dataStr.substring(22, 30)), "xxxxxx.xx") + "|m3");
                    list.add(tipStr + "剩余金额(总)|" + addSign(reverbyte(dataStr.substring(30, 38)), "xxxxxx.xx") + "|元");
                    list.add(tipStr + "剩余金额(本地)|" + addSign(reverbyte(dataStr.substring(38, 46)), "xxxxxx.xx") + "|元");
                    list.add(tipStr + "剩余金额(远程)|" + addSign(reverbyte(dataStr.substring(46, 54)), "xxxxxx.xx") + "|元");
                    break;
                case "7001FF01"://上1次调价信息
                case "7001FF02":
                case "7001FF03":
                case "7001FF04":
                case "7001FF05":
                    list.clear();
                    times = Integer.parseInt(cmd.substring(6, 8), 16);
                    tipStr = "上" + times + "调价";
                    list.add(tipStr + "时间|" + addSign(reverbyte(dataStr.substring(0, 6)), "YYMMDD") + "|");
                    list.add(tipStr + "阶梯气价1|" + addSign(reverbyte(dataStr.substring(6, 14)), "xxxxxx.xx") + "|元");
                    list.add(tipStr + "阶梯气价2|" + addSign(reverbyte(dataStr.substring(14, 22)), "xxxxxx.xx") + "|元");
                    list.add(tipStr + "阶梯气价3|" + addSign(reverbyte(dataStr.substring(22, 30)), "xxxxxx.xx") + "|元");
                    list.add(tipStr + "阶梯气价4|" + addSign(reverbyte(dataStr.substring(30, 38)), "xxxxxx.xx") + "|元");
                    list.add(tipStr + "阶梯1累计用气量|" + addSign(reverbyte(dataStr.substring(38, 46)), "xxxxxx.xx") + "|m3");
                    list.add(tipStr + "阶梯2累计用气量|" + addSign(reverbyte(dataStr.substring(46, 54)), "xxxxxx.xx") + "|m3");
                    list.add(tipStr + "阶梯3累计用气量|" + addSign(reverbyte(dataStr.substring(54, 62)), "xxxxxx.xx") + "|m3");
                    list.add(tipStr + "阶梯4累计用气量|" + addSign(reverbyte(dataStr.substring(62, 70)), "xxxxxx.xx") + "|m3");
                    break;
                case "7003FF00"://当前气价信息
                    list.clear();
                    // tipStr = "当前套";
                    list.add("实时气价|" + String.format("%.2f", Integer.parseInt(reverbyte(dataStr.substring(0, 8)), 16) * 0.01) + "|元");
                    list.add("阶梯气价1|" + String.format("%.2f", Integer.parseInt(reverbyte(dataStr.substring(8, 16)), 16) * 0.01) + "|元");
                    list.add("阶梯气价2|" + String.format("%.2f", Integer.parseInt(reverbyte(dataStr.substring(16, 24)), 16) * 0.01) + "|元");
                    list.add("阶梯气价3|" + String.format("%.2f", Integer.parseInt(reverbyte(dataStr.substring(24, 32)), 16) * 0.01) + "|元");
                    list.add("阶梯气价4|" + String.format("%.2f", Integer.parseInt(reverbyte(dataStr.substring(32, 40)), 16) * 0.01) + "|元");
                    list.add("阶梯值1|" + String.format("%.2f", Integer.parseInt(reverbyte(dataStr.substring(40, 48)), 16) * 0.01) + "|m3");
                    list.add("阶梯值2|" + String.format("%.2f", Integer.parseInt(reverbyte(dataStr.substring(48, 56)), 16) * 0.01) + "|m3");
                    list.add("阶梯值3|" + String.format("%.2f", Integer.parseInt(reverbyte(dataStr.substring(56, 64)), 16) * 0.01) + "|m3");
                    break;
                case "72010000"://抄读气表开阀次数
                    list.clear();
                    list.add("开阀次数|" + String.valueOf(Integer.parseInt(reverbyte(dataStr.substring(0, 4)), 16)) + "|");
                    break;
                case "7201FF01"://抄读气表上1开阀事件
                case "7201FF02":
                case "7201FF03":
                case "7201FF04":
                case "7201FF05":
                case "7201FF06":
                case "7201FF07":
                case "7201FF08":
                case "7201FF09":
                case "7201FF0A":
                    list.clear();
                    times = Integer.parseInt(cmd.substring(6, 8), 16);
                    tipStr = "上" + times + "开阀";
                    list.add(tipStr + "时间|" + addSign(reverbyte(dataStr.substring(0, 10)), "YYMMDDhhmm") + "|");
                    String temp = reverbyte(dataStr.substring(10, 12));
                    if (temp.equals("00"))
                        list.add(tipStr + "原因|" + "00:各关阀原因恢复" + "|");
                    else if (temp.equals("20"))
                        list.add(tipStr + "原因|" + "20:换电池" + "|");
                    else if (temp.equals("02"))
                        list.add(tipStr + "原因|" + "02:达到报警金额1" + "|");
                    else if (temp.equals("04"))
                        list.add(tipStr + "原因|" + "04:达到透支门限" + "|");
                    else if (temp.equals("08"))
                        list.add(tipStr + "原因|" + "08:ESAM坏" + "|");
                    else if (temp.equals("10"))
                        list.add(tipStr + "原因|" + "10:磁场干扰" + "|");
                    else if (temp.equals("40"))
                        list.add(tipStr + "原因|" + "40:大小流量关阀干簧管坏" + "|");
                    else if (temp.equals("80"))
                        list.add(tipStr + "原因|" + "80:远程开阀" + "|");
                    list.add(tipStr + "时累计用气量|" + addSign(reverbyte(dataStr.substring(12, 20)), "xxxxxx.xx") + "|m3");
                    list.add(tipStr + "时剩余金额|" + addSign(reverbyte(dataStr.substring(20, 28)), "xxxxxx.xx") + "|元");
                    break;
                case "72020000"://抄读气表关阀次数
                    list.clear();
                    list.add("关阀次数|" + String.valueOf(Integer.parseInt(reverbyte(dataStr.substring(0, 4)), 16)) + "|");
                    break;

                case "7202FF01"://抄读气表上1关阀事件
                case "7202FF02":
                case "7202FF03":
                case "7202FF04":
                case "7202FF05":
                case "7202FF06":
                case "7202FF07":
                case "7202FF08":
                case "7202FF09":
                case "7202FF0A":
                    list.clear();
                    times = Integer.parseInt(cmd.substring(6, 8), 16);
                    tipStr = "上" + times + "关阀";
                    list.add(tipStr + "时间|" + addSign(reverbyte(dataStr.substring(0, 10)), "YYMMDDhhmm") + "|");
                    temp = reverbyte(dataStr.substring(10, 12));
                    if (temp.equals("00"))
                        list.add(tipStr + "原因|" + "00:停用关阀 " + "|");
                    else if (temp.equals("01"))
                        list.add(tipStr + "原因|" + "01:电池电压低" + "|");
                    else if (temp.equals("02"))
                        list.add(tipStr + "原因|" + "02:达到报警金额1" + "|");
                    else if (temp.equals("04"))
                        list.add(tipStr + "原因|" + "04:达到透支门限" + "|");
                    else if (temp.equals("08"))
                        list.add(tipStr + "原因|" + "08:ESAM坏" + "|");
                    else if (temp.equals("10"))
                        list.add(tipStr + "原因|" + "10:磁场干扰" + "|");
                    else if (temp.equals("20"))
                        list.add(tipStr + "原因|" + "20:掉电关阀" + "|");
                    else if (temp.equals("40"))
                        list.add(tipStr + "原因|" + "40:大小流量关阀干簧管坏" + "|");
                    else if (temp.equals("80"))
                        list.add(tipStr + "原因|" + "80:远程关阀" + "|");
                    list.add(tipStr + "时累计用气量|" + addSign(reverbyte(dataStr.substring(12, 20)), "xxxxxx.xx") + "|m3");
                    list.add(tipStr + "时剩余金额|" + addSign(reverbyte(dataStr.substring(20, 28)), "xxxxxx.xx") + "|元");
                    break;
                case "72030000"://超大流量次数
                    list.clear();
                    list.add("超大流量次数|" + String.valueOf(Integer.parseInt(reverbyte(dataStr.substring(0, 4)), 16)) + "|");
                    break;
                case "7203FF01"://上1超大流量
                case "7203FF02":
                case "7203FF03":
                case "7203FF04":
                case "7203FF05":
                case "7203FF06":
                case "7203FF07":
                case "7203FF08":
                case "7203FF09":
                case "7203FF0A":
                    list.clear();
                    times = Integer.parseInt(cmd.substring(6, 8), 16);
                    tipStr = "上" + times + "超大流量";
                    list.add(tipStr + "发生时间|" + addSign(reverbyte(dataStr.substring(0, 10)), "YYMMDDhhmm") + "|");
                    list.add(tipStr + "发生时累计用气量|" + addSign(reverbyte(dataStr.substring(10, 18)), "xxxxxx.xx") + "|m3");
                    list.add(tipStr + "发生时剩余金额|" + addSign(reverbyte(dataStr.substring(18, 26)), "xxxxxx.xx") + "|元");
                    list.add(tipStr + "流量值|" + reverbyte(dataStr.substring(26, 28)) + "|");
                    list.add(tipStr + "结束时间|" + addSign(reverbyte(dataStr.substring(28, 38)), "YYMMDDhhmm") + "|");
                    list.add(tipStr + "结束时累计用气量|" + addSign(reverbyte(dataStr.substring(38, 46)), "xxxxxx.xx") + "|m3");
                    list.add(tipStr + "结束时剩余金额|" + addSign(reverbyte(dataStr.substring(46, 54)), "xxxxxx.xx") + "|元");
                    break;
                case "72040000"://超小流量次数
                    list.clear();
                    list.add("超小流量次数|" + String.valueOf(Integer.parseInt(reverbyte(dataStr.substring(0, 4)), 16)) + "|");
                    break;
                case "7204FF01"://上1超小流量
                case "7204FF02":
                case "7204FF03":
                case "7204FF04":
                case "7204FF05":
                case "7204FF06":
                case "7204FF07":
                case "7204FF08":
                case "7204FF09":
                case "7204FF0A":
                    list.clear();
                    times = Integer.parseInt(cmd.substring(6, 8), 16);
                    tipStr = "上" + times + "超小流量";
                    list.add(tipStr + "发生时间|" + addSign(reverbyte(dataStr.substring(0, 10)), "YYMMDDhhmm") + "|");
                    list.add(tipStr + "发生时累计用气量|" + addSign(reverbyte(dataStr.substring(10, 18)), "xxxxxx.xx") + "|m3");
                    list.add(tipStr + "发生时剩余金额|" + addSign(reverbyte(dataStr.substring(18, 26)), "xxxxxx.xx") + "|元");
                    list.add(tipStr + "流量值|" + reverbyte(dataStr.substring(26, 28)) + "|");
                    list.add(tipStr + "结束时间|" + addSign(reverbyte(dataStr.substring(28, 38)), "YYMMDDhhmm") + "|");
                    list.add(tipStr + "结束时累计用气量|" + addSign(reverbyte(dataStr.substring(38, 46)), "xxxxxx.xx") + "|m3");
                    list.add(tipStr + "结束时剩余金额|" + addSign(reverbyte(dataStr.substring(46, 54)), "xxxxxx.xx") + "|元");
                    break;
                case "72050000"://磁场干扰次数
                    list.clear();
                    list.add("磁场干扰次数|" + String.valueOf(Integer.parseInt(reverbyte(dataStr.substring(0, 4)), 16)) + "|");
                    break;
                case "7205FF01"://上1磁场干扰
                case "7205FF02":
                case "7205FF03":
                case "7205FF04":
                case "7205FF05":
                case "7205FF06":
                case "7205FF07":
                case "7205FF08":
                case "7205FF09":
                case "7205FF0A":
                    list.clear();
                    times = Integer.parseInt(cmd.substring(6, 8), 16);
                    tipStr = "上" + times + "磁场干扰";
                    list.add(tipStr + "发生时间|" + addSign(reverbyte(dataStr.substring(0, 10)), "YYMMDDhhmm") + "|");
                    list.add(tipStr + "发生时累计用气量|" + addSign(reverbyte(dataStr.substring(10, 18)), "xxxxxx.xx") + "|m3");
                    list.add(tipStr + "发生时剩余金额|" + addSign(reverbyte(dataStr.substring(18, 26)), "xxxxxx.xx") + "|元");
                    list.add(tipStr + "结束时间|" + addSign(reverbyte(dataStr.substring(26, 36)), "YYMMDDhhmm") + "|");
                    list.add(tipStr + "结束时累计用气量|" + addSign(reverbyte(dataStr.substring(36, 44)), "xxxxxx.xx") + "|m3");
                    list.add(tipStr + "结束时剩余金额|" + addSign(reverbyte(dataStr.substring(44, 52)), "xxxxxx.xx") + "|元");
                    break;
                case "72060000"://阀门异常次数
                    list.clear();
                    list.add("阀门异常次数|" + String.valueOf(Integer.parseInt(reverbyte(dataStr.substring(0, 4)), 16)) + "|");
                    break;
                case "7206FF01"://上1阀门异常
                case "7206FF02":
                case "7206FF03":
                case "7206FF04":
                case "7206FF05":
                case "7206FF06":
                case "7206FF07":
                case "7206FF08":
                case "7206FF09":
                case "7206FF0A":
                    list.clear();
                    times = Integer.parseInt(cmd.substring(6, 8), 16);
                    tipStr = "上" + times + "阀门异常";
                    list.add(tipStr + "发生时间|" + addSign(reverbyte(dataStr.substring(0, 10)), "YYMMDDhhmm") + "|");
                    list.add(tipStr + "发生时累计用气量|" + addSign(reverbyte(dataStr.substring(10, 18)), "xxxxxx.xx") + "|m3");
                    list.add(tipStr + "发生时剩余金额|" + addSign(reverbyte(dataStr.substring(18, 26)), "xxxxxx.xx") + "|元");
                    temp = reverbyte(dataStr.substring(26, 28));
                    if (temp.equals("00")) {
                        list.add(tipStr + "状态|" + "00:开" + "|");
                    } else if (temp.equals("01")) {
                        list.add(tipStr + "状态|" + "01:关" + "|");
                    }
                    list.add(tipStr + "结束时间|" + addSign(reverbyte(dataStr.substring(28, 38)), "YYMMDDhhmm") + "|");
                    list.add(tipStr + "结束时累计用气量|" + addSign(reverbyte(dataStr.substring(38, 46)), "xxxxxx.xx") + "|m3");
                    list.add(tipStr + "结束时剩余金额|" + addSign(reverbyte(dataStr.substring(46, 54)), "xxxxxx.xx") + "|元");
                    break;
                case "710100FF"://气表参数信息
                    list.clear();
                    //   list.add("用气类型|"+reverbyte(dataStr.substring(0,2))+"|");
                    list.add("报警限额1|" + String.format("%.2f", Integer.parseInt(reverbyte(dataStr.substring(2, 10)), 16) * 0.01) + "|元");
                    list.add("报警限额2|" + String.format("%.2f", Integer.parseInt(reverbyte(dataStr.substring(10, 18)), 16) * 0.01) + "|元");
                    list.add("充值限额|" + String.format("%.2f", Integer.parseInt(reverbyte(dataStr.substring(18, 26)), 16) * 0.01) + "|元");
                    list.add("月统计日期|" + addSign(reverbyte(dataStr.substring(26, 30)), "DDhh") + "|");
                    list.add("大流量门限次数|" + String.valueOf(Integer.parseInt(reverbyte(dataStr.substring(30, 32)), 16)) + "|次");
                    list.add("大流量门限时间|" + String.valueOf(Integer.parseInt(reverbyte(dataStr.substring(32, 34)), 16)) + "|秒");
                    list.add("小流量门限次数|" + String.valueOf(Integer.parseInt(reverbyte(dataStr.substring(34, 36)), 16)) + "|次");
                    list.add("小流量门限时间|" + String.valueOf(Integer.parseInt(reverbyte(dataStr.substring(36, 38)), 16)) + "|秒");
                    list.add("停用关阀时间|" + String.valueOf(Integer.parseInt(reverbyte(dataStr.substring(38, 42)), 16)) + "|天");
                    list.add("燃气表表号|" + reverbyte(dataStr.substring(42, 58)) + "|");
                    list.add("用户号|" + reverbyte(dataStr.substring(58, 70)) + "|");
                    break;
                case "710200FF"://当前套价格
                case "710300FF"://备用套
                    list.clear();
                    list.add("价格版本号|" + String.valueOf(Integer.parseInt(reverbyte(dataStr.substring(0, 4)), 16)) + "|");
                    list.add("第一套阶梯值1|" + String.format("%.2f", Integer.parseInt(reverbyte(dataStr.substring(4, 12)), 16) * 0.01) + "|m3");
                    list.add("第一套阶梯值2|" + String.format("%.2f", Integer.parseInt(reverbyte(dataStr.substring(12, 20)), 16) * 0.01) + "|m3");
                    list.add("第一套阶梯值3|" + String.format("%.2f", Integer.parseInt(reverbyte(dataStr.substring(20, 28)), 16) * 0.01) + "|m3");
                    list.add("第一套阶气价1|" + String.format("%.2f", Integer.parseInt(reverbyte(dataStr.substring(52, 60)), 16) * 0.01) + "|元");
                    list.add("第一套阶气价2|" + String.format("%.2f", Integer.parseInt(reverbyte(dataStr.substring(60, 68)), 16) * 0.01) + "|元");
                    list.add("第一套阶气价3|" + String.format("%.2f", Integer.parseInt(reverbyte(dataStr.substring(68, 76)), 16) * 0.01) + "|元");
                    list.add("第一套阶气价4|" + String.format("%.2f", Integer.parseInt(reverbyte(dataStr.substring(76, 84)), 16) * 0.01) + "|元");
                    list.add("第二套阶梯值1|" + String.format("%.2f", Integer.parseInt(reverbyte(dataStr.substring(108, 116)), 16) * 0.01) + "|m3");
                    list.add("第二套阶梯值2|" + String.format("%.2f", Integer.parseInt(reverbyte(dataStr.substring(116, 124)), 16) * 0.01) + "|m3");
                    list.add("第二套阶梯值3|" + String.format("%.2f", Integer.parseInt(reverbyte(dataStr.substring(124, 132)), 16) * 0.01) + "|m3");
                    list.add("第二套阶气价1|" + String.format("%.2f", Integer.parseInt(reverbyte(dataStr.substring(156, 164)), 16) * 0.01) + "|元");
                    list.add("第二套阶气价2|" + String.format("%.2f", Integer.parseInt(reverbyte(dataStr.substring(164, 172)), 16) * 0.01) + "|元");
                    list.add("第二套阶气价3|" + String.format("%.2f", Integer.parseInt(reverbyte(dataStr.substring(172, 180)), 16) * 0.01) + "|元");
                    list.add("第二套阶气价4|" + String.format("%.2f", Integer.parseInt(reverbyte(dataStr.substring(180, 188)), 16) * 0.01) + "|元");
                    list.add("时段表1|" + addSign(reverbyte(dataStr.substring(212, 218)), "MMDDNN") + "|");
                    list.add("时段表2|" + addSign(reverbyte(dataStr.substring(218, 224)), "MMDDNN") + "|");
                    list.add("时段表3|" + addSign(reverbyte(dataStr.substring(224, 230)), "MMDDNN") + "|");
                    list.add("时段表4|" + addSign(reverbyte(dataStr.substring(230, 236)), "MMDDNN") + "|");
                    temp = reverbyte(dataStr.substring(236, 238));
                    if (temp.equals("00")) {
                        list.add("结算方式|" + "00:月结算|");
                    } else if (temp.equals("01")) {
                        list.add("结算方式|" + "01:季度结算|");
                    } else if (temp.equals("02")) {
                        list.add("结算方式|" + "02:年结算|");
                    }
                    list.add("月结算日|" + addSign(reverbyte(dataStr.substring(238, 242)), "DDhh") + "|");
                    list.add("季度结算日1|" + addSign(reverbyte(dataStr.substring(242, 246)), "MMDD") + "|");
                    list.add("季度结算日2|" + addSign(reverbyte(dataStr.substring(246, 250)), "MMDD") + "|");
                    list.add("季度结算日3|" + addSign(reverbyte(dataStr.substring(250, 254)), "MMDD") + "|");
                    list.add("季度结算日4|" + addSign(reverbyte(dataStr.substring(254, 258)), "MMDD") + "|");
                    list.add("年结算日|" + addSign(reverbyte(dataStr.substring(258, 262)), "MMDD") + "|");
                    list.add("切换时间|" + addSign(reverbyte(dataStr.substring(262, 268)), "YYMMDD") + "|");
                    break;
            }
//            isUpLoad = 0;
//            String cmdName = set_command_select.getText().toString();
//            String strAddr = CommonUtil.AddZeros(addr.getText().toString());
//
//            if (list.size() > 1)
//                insertDataGas(strAddr, cmdName, CMD, isUpLoad, list);//抄表数据入库
        } else if (controlCode.equals("02")) {
            try {
                list.clear();
                list.add("编写|成功");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (controlCode.equals("07")) {
            try {
                list.clear();
                list.add("控制|成功");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                list.clear();
                list.add("编写/控制|失败");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    private String reverbyte1(String data)//每个字符倒置
    {
        byte[] bytes = data.getBytes();
        byte[] bytes1 = new byte[bytes.length];
        for (int i = 0; i <= bytes.length - 1; i++) {
            bytes1[i] = bytes[bytes.length - i - 1];
        }
        return new String(bytes1);
    }

    //  补零12位
    public String AddZeros(String data) {
        if (data.length() == 12) {
            return data;
        } else {
            String str = "000000000000";
            String str_m = str.substring(0, 12 - data.length()) + data;
            return str_m;
        }
    }

    public static String AddZeros(String data, int len) {
        int i = len - data.length();
        String str_m = data;
        for (int j = 0; j < i; j++)
            str_m = "0" + str_m;
        return str_m;
        // System.out.println(str_m);
    }

    private static String reverbyte(String data) {
        byte[] bytes = data.getBytes();
        byte[] bytes1 = new byte[bytes.length];
        for (int i = 0; i < bytes.length / 2; i++) {
            bytes1[i * 2] = bytes[bytes.length - 1 - i * 2 - 1];
            bytes1[i * 2 + 1] = bytes[bytes.length - 1 - i * 2];
        }
        return new String(bytes1);
    }

    private String addSign(String inStr, String ym) {
        int i = ym.indexOf('.');
        String outStr = "";
        try {
            if (i >= 0) {
                outStr = String.valueOf(Integer.parseInt(inStr.substring(0, i))) + "." + inStr.substring(i);
            } else {
                if (ym == "YYMMDDhhmm") {
                    outStr = inStr.substring(0, 2) + "-" + inStr.substring(2, 4) + "-" + inStr.substring(4, 6) + " "
                            + inStr.substring(6, 8) + ":" + inStr.substring(8, 10);
                } else if (ym == "YYMMDDWWhhmmss") {
                    String week = inStr.substring(7, 8);
                    if (week.equals("7"))
                        week = "星期日";
                    if (week.equals("1"))
                        week = "星期一";
                    if (week.equals("2"))
                        week = "星期二";
                    if (week.equals("3"))
                        week = "星期三";
                    if (week.equals("4"))
                        week = "星期四";
                    if (week.equals("5"))
                        week = "星期五";
                    if (week.equals("6"))
                        week = "星期六";
                    outStr = inStr.substring(0, 2) + "-" + inStr.substring(2, 4) + "-" + inStr.substring(4, 6) + " " + week
                            + " " + inStr.substring(8, 10) + ":" + inStr.substring(10, 12) + ":" + inStr.substring(12, 14);
                } else if (ym == "MMDDNN") {
                    outStr = inStr.substring(0, 2) + "月" + inStr.substring(2, 4) + "日" + " " + "阶梯编号 " + inStr.substring(4, 6);
                } else if (ym == "YYMMDD") {
                    outStr = inStr.substring(0, 2) + "年" + inStr.substring(2, 4) + "月" + inStr.substring(4, 6) + "日";
                } else if (ym == "MMDD") {
                    outStr = inStr.substring(0, 2) + "月" + inStr.substring(2, 4) + "日";
                } else if (ym == "DDhh") {
                    outStr = inStr.substring(0, 2) + "日" + inStr.substring(2, 4) + "时";
                } else {
                    outStr = String.valueOf(Integer.parseInt(inStr));
                }
            }
        } catch (Exception e) {
            outStr = "";
        }
        return outStr;
    }

    private List<Map<String, Object>> getData(ArrayList<String> lst) {
        //map.put(参数名字,参数值)
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> map;
        for (int i = 0; i < lst.size(); i++) {
            String str = lst.get(i);
            String[] arrStr = str.split("\\|");
            map = new HashMap<String, Object>();
            for (int j = 0; j < arrStr.length; j++) {
                if (j == 0)
                    map.put("name", arrStr[j]);
                if (j == 1)
                    map.put("data", arrStr[j]);
                if (j == 2)
                    map.put("unit", arrStr[j]);
            }
            list.add(map);
        }
        return list;
    }//最终生成map的数据

    private BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            //There are four basic operations for moving data in BLE: read, write, notify,
            // and indicate. The BLE protocol specification requires that the maximum data
            // payload size for these operations is 20 bytes, or in the case of read operations,
            // 22 bytes. BLE is built for low power consumption, for infrequent short-burst data transmissions.
            // Sending lots of data is possible, but usually ends up being less efficient than classic Bluetooth
            // when trying to achieve maximum throughput.  从google查找的，解释了为什么android下notify无法解释超过
            //20个字节的数据
            Bundle extras = intent.getExtras();
            if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
                // Data Received
                if (extras.containsKey(Constants.EXTRA_BYTE_VALUE)) {
                    if (extras.containsKey(Constants.EXTRA_BYTE_UUID_VALUE)) {
                        if (myApplication != null) {
                            BluetoothGattCharacteristic requiredCharacteristic = myApplication.getCharacteristic();
                            String uuidRequired = requiredCharacteristic.getUuid().toString();
                            String receivedUUID = intent.getStringExtra(Constants.EXTRA_BYTE_UUID_VALUE);

                            if (isDebugMode) {
                                byte[] array = intent.getByteArrayExtra(Constants.EXTRA_BYTE_VALUE);
                                Message msg = new Message(Message.MESSAGE_TYPE.RECEIVE, formatMsgContent(array));
                                notifyAdapter(msg);
                            } else if (uuidRequired.equalsIgnoreCase(receivedUUID)) {
                                byte[] array = intent.getByteArrayExtra(Constants.EXTRA_BYTE_VALUE);
                                String tempStr = Utils.byteToASCII(array);
                                sb.append(tempStr);
                                Log.d("recive", sb.toString());
                                DltGas645 gas645 = new DltGas645();
                                int bz = gas645.AnalyseData(sb.toString().trim(), editAddr.getText().toString().trim());
                                Log.d("recivebz", String.valueOf(bz));
                                if (bz == 0) {
                                    String[] from = {"name", "data", "unit"};
                                    int[] to = {R.id.item1, R.id.item2, R.id.item3,};
                                    ArrayList<String> list = new ArrayList<String>();
                                    list = MakeData(gas645.recvStr);
                                    List<Map<String, Object>> data_list = new ArrayList<Map<String, Object>>();
                                    data_list = getData(list);
                                    simpleAdapter = new SimpleAdapter(GattDetailActivity.this, data_list, R.layout.item_bledata, from, to);
                                    listview.setAdapter(simpleAdapter);            //配置适配器
                                    sb.delete(0, sb.length());
                                } else if (bz == 1) {
                                    Toast.makeText(GattDetailActivity.this, "返回信息头不完整！",
                                            Toast.LENGTH_SHORT).show();
                                } else if (bz == 2) {
                                    Toast.makeText(context, "返回表号不正确！",
                                            Toast.LENGTH_SHORT).show();
                                } else if (bz == 3) {
                                    Toast.makeText(context, "返回校验位不正确！",
                                            Toast.LENGTH_SHORT).show();
                                } else if (bz == 4) {
                                    Toast.makeText(context, "返回信息不完整！",
                                            Toast.LENGTH_SHORT).show();
                                }
                                Message msg = new Message(Message.MESSAGE_TYPE.RECEIVE, formatMsgContent(array));
                                notifyAdapter(msg);
                            }
                        }
                    }
                }


                if (extras.containsKey(Constants.EXTRA_DESCRIPTOR_BYTE_VALUE)) {
                    if (extras.containsKey(Constants.EXTRA_DESCRIPTOR_BYTE_VALUE_CHARACTERISTIC_UUID)) {
                        BluetoothGattCharacteristic requiredCharacteristic = myApplication.
                                getCharacteristic();
                        String uuidRequired = requiredCharacteristic.getUuid().toString();
                        String receivedUUID = intent.getStringExtra(
                                Constants.EXTRA_DESCRIPTOR_BYTE_VALUE_CHARACTERISTIC_UUID);

                        byte[] array = intent
                                .getByteArrayExtra(Constants.EXTRA_DESCRIPTOR_BYTE_VALUE);

//                        System.out.println("GattDetailActivity---------------------->descriptor:" + Utils.ByteArraytoHex(array));
                        if (isDebugMode) {
                            updateButtonStatus(array);
                        } else if (uuidRequired.equalsIgnoreCase(receivedUUID)) {
                            updateButtonStatus(array);
                        }

                    }
                }
            }

            if (action.equals(BluetoothLeService.ACTION_GATT_DESCRIPTORWRITE_RESULT)) {
                if (extras.containsKey(Constants.EXTRA_DESCRIPTOR_WRITE_RESULT)) {
                    int status = extras.getInt(Constants.EXTRA_DESCRIPTOR_WRITE_RESULT);
                    if (status != BluetoothGatt.GATT_SUCCESS) {
                        Snackbar.make(rlContent, R.string.option_fail, Snackbar.LENGTH_LONG).show();
                    }
                }
            }

            if (action.equals(BluetoothLeService.ACTION_GATT_CHARACTERISTIC_ERROR)) {
                if (extras.containsKey(Constants.EXTRA_CHARACTERISTIC_ERROR_MESSAGE)) {
                    String errorMessage = extras.
                            getString(Constants.EXTRA_CHARACTERISTIC_ERROR_MESSAGE);
                    System.out.println("GattDetailActivity---------------------->err:" + errorMessage);
                    showDialog(errorMessage);
                }

            }

            //write characteristics succcess
            if (action.equals(BluetoothLeService.ACTION_GATT_CHARACTERISTIC_WRITE_SUCCESS)) {
                list.get(list.size() - 1).setDone(true);
                adapter.notifyItemChanged(list.size() - 1);
            }

            if (action.equals(BluetoothDevice.ACTION_BOND_STATE_CHANGED)) {
//                final int state = intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, BluetoothDevice.ERROR);
//                if (state == BluetoothDevice.BOND_BONDING) {}
//                else if (state == BluetoothDevice.BOND_BONDED) {}
//                else if (state == BluetoothDevice.BOND_NONE) {}
            }

            //connect break (连接断开)
            if (action.equals(BluetoothLeService.ACTION_GATT_DISCONNECTED)) {
                showDialog(getString(R.string.conn_disconnected));
            }
        }

    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gatt_detail);
        ButterKnife.bind(this);
        bindToolBar();
        myApplication = (MyApplication) getApplication();
        optionsMenuManager = OptionsMenuManager.getInstance();

//        recvEdit = (EditText) findViewById(R.id.recvEdit);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        rvMsg.setLayoutManager(llm);

        adapter = new MessagesAdapter(this, list);
        rvMsg.setAdapter(adapter);

        initCharacteristics();
        initProperties();

        registerReceiver(mGattUpdateReceiver, Utils.makeGattUpdateIntentFilter());

        rvMsg.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                if (optionsMenuManager.getOptionsMenu() != null)
                    dismissMenu();
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });

        if (savedInstanceState == null) {
            filterView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    filterView.getViewTreeObserver().removeOnPreDrawListener(this);
                    startEndAnimation();
                    return true;
                }
            });
        }
        btnSelect.setOnClickListener(new View.OnClickListener() {//命令选择事件
            @Override
            public void onClick(View v) {
                ArrayAdapter<String> adapter;
                final String[] datas = {"71040001——读通信地址",
                        "0400010C——抄读气表时间",
                        "7000FF00——抄读气表当前使用量",
                        "7200FF00——抄读气表实时状态查询",
                        "710100FF——抄读气表参数信息",
                        "710200FF——抄读当前套气价信息",
                        "710300FF——抄读备用套气价信息",
                        "7002FF01——抄读气表上1月冻结数据",
                        "7002FF02——抄读气表上2月冻结数据",
                        "7002FF03——抄读气表上3月冻结数据",
                        "7002FF04——抄读气表上4月冻结数据",
                        "7002FF05——抄读气表上5月冻结数据",
                        "7000FF01——抄读气表上1结算数据",
                        "7000FF02——抄读气表上2结算数据",
                        "7000FF03——抄读气表上3结算数据",
                        "7000FF04——抄读气表上4结算数据",
                        "7000FF05——抄读气表上5结算数据",
                        "7001FF01——抄读气表上1次调价信息",
                        "7001FF02——抄读气表上2次调价信息",
                        "7001FF03——抄读气表上3次调价信息",
                        "7001FF04——抄读气表上4次调价信息",
                        "7001FF05——抄读气表上5次调价信息",
                        "7003FF00——抄读气表当前气价信息",
                        "72010000——抄读气表开阀次数",
                        "7201FF01——抄读气表上1开阀事件",
                        "7201FF02——抄读气表上2开阀事件",
                        "7201FF03——抄读气表上3开阀事件",
                        "7201FF04——抄读气表上4开阀事件",
                        "7201FF05——抄读气表上5开阀事件",
                        "72020000——抄读气表关阀次数",
                        "7202FF01——抄读气表上1关阀事件",
                        "7202FF02——抄读气表上2关阀事件",
                        "7202FF03——抄读气表上3关阀事件",
                        "7202FF04——抄读气表上4关阀事件",
                        "7202FF05——抄读气表上5关阀事件",
                        "72030000——抄读气表超大流量次数",
                        "7203FF01——抄读气表上1超大流量事件",
                        "7203FF02——抄读气表上2超大流量事件",
                        "7203FF03——抄读气表上3超大流量事件",
                        "7203FF04——抄读气表上4超大流量事件",
                        "7203FF05——抄读气表上5超大流量事件",
                        "72040000——抄读气表超小流量次数",
                        "7204FF01——抄读气表上1超小流量事件",
                        "7204FF02——抄读气表上2超小流量事件",
                        "7204FF03——抄读气表上3超小流量事件",
                        "7204FF04——抄读气表上4超小流量事件",
                        "7204FF05——抄读气表上5超小流量事件",
                        "72050000——抄读气表磁场干扰次数",
                        "7205FF01——抄读气表上1磁场干扰事件",
                        "7205FF02——抄读气表上2磁场干扰事件",
                        "7205FF03——抄读气表上3磁场干扰事件",
                        "7205FF04——抄读气表上4磁场干扰事件",
                        "7205FF05——抄读气表上5磁场干扰事件",
                        "72060000——抄读气表阀门异常次数",
                        "7206FF01——抄读气表上1阀门异常事件",
                        "7206FF02——抄读气表上2阀门异常事件",
                        "7206FF03——抄读气表上3阀门异常事件",
                        "7206FF04——抄读气表上4阀门异常事件",
                        "7206FF05——抄读气表上5阀门异常事件"};
                adapter = new ArrayAdapter<String>(GattDetailActivity.this, android.R.layout.simple_list_item_1, datas);
                AlertDialog.Builder builder = new AlertDialog.Builder(GattDetailActivity.this);
//                builder.setIcon(R.drawable.button);
                builder.setTitle("提示：");
                builder.setAdapter(adapter, new OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
//                        Toast.makeText(GattDetailActivity.this, "你点击了第"+which+"个item", 1000).show();
                        DltGas645 gas645 = new DltGas645(AddZeros(editAddr.getText().toString()), "01", datas[which].toString().substring(0, 8), "");
                        btnSelect.setText(datas[which].toString());
                        etWrite.setText(gas645.CreateFrame());
                    }
                });
                builder.show();

            }
        });
    }

    private void initCharacteristics() {
        BluetoothGattCharacteristic characteristic = myApplication.getCharacteristic();
        if (characteristic.getUuid().toString().equals(GattAttributes.USR_SERVICE)) {
            isDebugMode = true;
            List<BluetoothGattCharacteristic> characteristics = ((MyApplication) getApplication()).getCharacteristics();

            for (BluetoothGattCharacteristic c : characteristics) {
                if (Utils.getPorperties(this, c).equals("Notify")) {
                    notifyCharacteristic = c;
                    continue;
                }

                if (Utils.getPorperties(this, c).equals("Write")) {
                    writeCharacteristic = c;
                    continue;
                }
            }

            properties = "Notify & Write";

        } else {
            properties = Utils.getPorperties(this, characteristic);

            notifyCharacteristic = characteristic;
            readCharacteristic = characteristic;
            writeCharacteristic = characteristic;
            indicateCharacteristic = characteristic;
        }
    }


    private void initProperties() {
        if (TextUtils.isEmpty(properties))
            return;
        tvProperties.setText(properties);
        String[] property = properties.split("&");

        if (property.length == 1) {
            btnOptions.setVisibility(View.GONE);
            Option option = new Option(properties.trim(), Option.OPTIONS_MAP.get(properties.trim()));
            setOption(option);
        } else {
            for (int i = 0; i < property.length; i++) {
                String p = property[i];
                Option option = new Option();
                option.setName(p.trim());
                option.setPropertyType(Option.OPTIONS_MAP.get(p.trim()));
                options.add(option);
                if (i == 0) {
                    setOption(option);
                }
            }
        }
    }


    private void setOption(Option option) {//选择Characteritics,NOtify,Write
        currentOption = option;
        switch (option.getPropertyType()) {
            case PROPERTY_NOTIFY:
                if (!nofityEnable)
                    btnOption.setText(Option.NOTIFY);
                else
                    btnOption.setText(Option.STOP_NOTIFY);
                showViewIsEdit(false);
                break;
            case PROPERTY_READ:
                btnOption.setText(Option.READ);
                showViewIsEdit(false);
                break;
            case PROPERTY_INDICATE:
                if (!indicateEnable)
                    btnOption.setText(Option.INDICATE);
                else
                    btnOption.setText(Option.STOP_INDICATE);
                showViewIsEdit(false);
                break;
            case PROPERTY_WRITE:
                showViewIsEdit(true);

                ///////////打开notifiy////////////////
                prepareBroadcastDataNotify(notifyCharacteristic);//开启关闭通知
                ///////////打开notifiy////////////////
                break;
        }
    }


    private void showViewIsEdit(boolean isEdit) {//
        if (isEdit) {
            btnOption.setVisibility(View.GONE);
            rlWrite.setVisibility(View.VISIBLE);
        } else {
            btnOption.setVisibility(View.VISIBLE);
            rlWrite.setVisibility(View.GONE);
        }
    }


    @OnClick(R.id.btn_options)
    public void onOptionsClick() {
        optionsMenuManager.toggleContextMenuFromView(options, btnOptions, new OptionsSelectAdapter.OptionsOnItemSelectedListener() {
            @Override
            public void onItemSelected(int position) {
                dismissMenu();
                setOption(options.get(position));
            }
        });
    }


    @OnClick(R.id.btn_option)
    public void onOptionClick() {
        if (optionsMenuManager.getOptionsMenu() != null) {
            dismissMenu();
            return;
        }
        switch (currentOption.getPropertyType()) {
            case PROPERTY_NOTIFY:
                notifyOption();//开启关闭通知
                break;
            case PROPERTY_INDICATE:
                indicateOption();
                break;
            case PROPERTY_READ:
                readOption();
                break;
            case PROPERTY_WRITE:
                break;
        }
    }


    @OnClick(R.id.btn_send)
    public void onSendClick() {
        writeOption(0);
    }

    @OnClick(R.id.btn_ggpl)
    public void onGgpl() {
        writeOption(1);
    }

    private void notifyOption() {//开启关闭通知
        if (nofityEnable) {
            nofityEnable = false;
            btnOption.setText(Option.NOTIFY);
            stopBroadcastDataNotify(notifyCharacteristic);
            Message msg = new Message(Message.MESSAGE_TYPE.SEND, Option.STOP_NOTIFY);
            notifyAdapter(msg);
        } else {
            nofityEnable = true;
            btnOption.setText(Option.STOP_NOTIFY);
            prepareBroadcastDataNotify(notifyCharacteristic);
            Message msg = new Message(Message.MESSAGE_TYPE.SEND, Option.NOTIFY);
            notifyAdapter(msg);
        }
    }


    private void indicateOption() {
        if (indicateEnable) {
            indicateEnable = false;
            btnOption.setText(Option.INDICATE);
            stopBroadcastDataIndicate(indicateCharacteristic);
            Message msg = new Message(Message.MESSAGE_TYPE.SEND, Option.STOP_INDICATE);
            notifyAdapter(msg);
        } else {
            nofityEnable = true;
            btnOption.setText(Option.STOP_INDICATE);
            prepareBroadcastDataIndicate(indicateCharacteristic);
            Message msg = new Message(Message.MESSAGE_TYPE.SEND, Option.INDICATE);
            notifyAdapter(msg);
        }
    }


    private void readOption() {
        Message msg = new Message(Message.MESSAGE_TYPE.SEND, Option.READ);
        notifyAdapter(msg);
        prepareBroadcastDataRead(readCharacteristic);
    }

    private void writeOption(int flag) {
        String text = null;
//        if (flag == 0) {
////            text = etWrite.getText().toString();
//            text = "6806105968010008176840010400FF00707b16";
//        } else {
//            //更改频率
//            text = "68F104000b0904037816";
//        }

        text = etWrite.getText().toString().trim();
        if(text.isEmpty()){
            text = "680630020030061820684001040C010004CC16";
        }
        writeOption(text);
    }

    private void writeOption(String text) {

        if (TextUtils.isEmpty(text)) {
            AnimateUtils.shake(etWrite);
//            return;
        }

        if (isHexSend) {
            text = text.replace(" ", "");
            if (!Utils.isRightHexStr(text)) {
                AnimateUtils.shake(etWrite);
                return;
            }
//            byte[] array = Utils.hexStringToByteArray(text);
//            writeCharacteristic(writeCharacteristic, array);
            StringBuilder sb = new StringBuilder();
            sb.append(text);
            String scommond;

            byte[] array;
            while (sb.length() != 0) {
                if (sb.length() > 40) {
                    scommond = sb.substring(0, 40);
                    sb.delete(0, 40);
                    array = Utils.hexStringToByteArray(scommond);
                    writeCharacteristic(writeCharacteristic, array);
                    try {
                        sleep(50); //暂停，每一秒输出一次
                    } catch (InterruptedException e) {
                        return;
                    }
                } else {
                    scommond = sb.toString();
                    sb.delete(0, sb.length());
                    array = Utils.hexStringToByteArray(scommond);
                    writeCharacteristic(writeCharacteristic, array);
                }

            }












        } else {
            try {
                int j = text.length() / 20;
                if ((text.length() % 20) > 0)
                    j++;
                int s = 0;
                int e = 0;
                for (int i = 0; i < j; i++) {
                    e = e + 20;
                    if (e > text.length())
                        e = text.length();
                    byte[] array = text.substring(s, e).getBytes("US-ASCII");
                    writeCharacteristic(writeCharacteristic, array);
                    try {
                        sleep(50); //暂停，每一秒输出一次
                    } catch (InterruptedException ex) {
                        return;
                    }
                    s = e;
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                System.out.println("--------------------->write text exception");
                return;
            }

        }

        Message msg = new Message(Message.MESSAGE_TYPE.SEND, text);
        notifyAdapter(msg);
    }


    /**
     * update option button status (更新Option按钮的操作状态)
     *
     * @param array
     */
    private void updateButtonStatus(byte[] array) {
        int status = array[0];
        switch (status) {
            case 0:
                if (btnOption.getText().toString().equals(Option.STOP_NOTIFY)) {
                    btnOption.setText(Option.NOTIFY);
                    Message msg = new Message(Message.MESSAGE_TYPE.RECEIVE, Option.STOP_NOTIFY);
                    notifyAdapter(msg);
                }

                if (btnOption.getText().toString().equals(Option.STOP_INDICATE)) {
                    btnOption.setText(Option.INDICATE);
                    Message msg = new Message(Message.MESSAGE_TYPE.RECEIVE, Option.STOP_INDICATE);
                    notifyAdapter(msg);
                }
                break;
            case 1:
                if (btnOption.getText().toString().equals(Option.NOTIFY)) {
                    btnOption.setText(Option.STOP_NOTIFY);
                    Message msg = new Message(Message.MESSAGE_TYPE.RECEIVE, Option.NOTIFY);
                    notifyAdapter(msg);
                }
                break;
            case 2:
                if (btnOption.getText().toString().equals(Option.INDICATE)) {
                    btnOption.setText(Option.STOP_INDICATE);
                    Message msg = new Message(Message.MESSAGE_TYPE.RECEIVE, Option.INDICATE);
                    notifyAdapter(msg);
                }
                break;
        }
    }


    private void startEndAnimation() {

        filterView.setAlpha(0.0f);
        filterView.setVisibility(View.VISIBLE);
        filterView.setLayerType(View.LAYER_TYPE_HARDWARE, null);

        ObjectAnimator animator1 = ObjectAnimator.ofInt(filterView, "backgroundColor",
                Color.parseColor("#0277bd"), Color.parseColor("#009688"));
        animator1.setDuration(200);
        animator1.setEvaluator(new ArgbEvaluator());


        filterView.animate()
                .alpha(0.6f)
                .setDuration(200)
                .setInterpolator(new AccelerateInterpolator())
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        tvProperties.setVisibility(View.VISIBLE);
                        rvMsg.setVisibility(View.VISIBLE);
                        rlBottom.setVisibility(View.VISIBLE);
                        topShadow.setVisibility(View.VISIBLE);
                        bottomShadow.setVisibility(View.VISIBLE);

                        tvProperties.setTranslationY(-Utils.dpToPx(40));
                        topShadow.setTranslationY(-Utils.dpToPx(40));
                        bottomShadow.setAlpha(0.0f);
                        rlBottom.setTranslationY(Utils.dpToPx(56));
                        btnOptions.setTranslationY(Utils.dpToPx(56));
                        AnimateUtils.translationY(rlBottom, 0, 300, 200);
                        AnimateUtils.alpha(bottomShadow, 0.3f, 100, 450);
                        AnimateUtils.translationY(btnOptions, 0, 300, 300);
                        AnimateUtils.translationY(tvProperties, 0, 300, 300);
                        AnimateUtils.translationY(topShadow, 0, 300, 300);
                        if (currentOption.getPropertyType() == Option.OPTION_PROPERTY.PROPERTY_WRITE) {
                            etWrite.setTranslationY(Utils.dpToPx(56));
                            btnSend.setTranslationY(Utils.dpToPx(56));
                            btnGgpl.setTranslationY(Utils.dpToPx(56));
                            AnimateUtils.translationY(etWrite, 0, 300, 400);
                            AnimateUtils.translationY(btnSend, 0, 300, 500);
                            AnimateUtils.translationY(btnGgpl, 0, 300, 500);
                        } else {
                            btnOption.setTranslationY(Utils.dpToPx(56));
                            AnimateUtils.translationY(btnOption, 0, 300, 500);
                        }

                        animate2();
                    }
                })
                .start();

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setAlpha(0.0f);
            AnimateUtils.alpha(toolbar, 1.0f, 200, 0);
        }

        animator1.start();
    }

    private void animate2() {
        filterView.animate()
                .alpha(0.0f)
                .setDuration(200)
                .setInterpolator(new AccelerateInterpolator())
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        filterView.setLayerType(View.LAYER_TYPE_NONE, null);
                        filterView.setVisibility(View.GONE);
                    }
                })
                .start();
    }


    private void notifyAdapter(Message msg) {
        list.add(msg);
        adapter.notifyLastItem();
        rvMsg.smoothScrollToPosition(adapter.getItemCount() - 1);
    }


    private void dismissMenu() {
        if (optionsMenuManager.getOptionsMenu() != null) {
            optionsMenuManager.toggleContextMenuFromView(null, null, null);
        }
    }


    private String formatMsgContent(byte[] data) {
//        return "HEX:"+Utils.ByteArraytoHex(data)+"  (ASSCII:"+Utils.byteToASCII(data)+")";
        return Utils.byteToASCII(data);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_more, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (optionsMenuManager.getOptionsMenu() != null) {
            dismissMenu();
            return false;
        }
        super.onOptionsItemSelected(item);
        String text = etWrite.getText().toString();
        switch (item.getItemId()) {
            case R.id.menu_hex_send:
                isHexSend = true;
                if (!TextUtils.isEmpty(text)) {
                    try {
                        etWrite.setText(Utils.ByteArraytoHex(text.getBytes("US-ASCII")));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                        etWrite.setText("");
                    }
                }
                break;
            case R.id.menu_asscii_send:
                isHexSend = false;
                etWrite.setText("");
                break;
            case R.id.menu_clear_display:
                list.clear();
                adapter.notifyDataSetChanged();
                break;
        }

        return false;
    }

    /**
     * Preparing Broadcast receiver to broadcast read characteristics
     *
     * @param characteristic
     */
    void prepareBroadcastDataRead(
            BluetoothGattCharacteristic characteristic) {
        final int charaProp = characteristic.getProperties();
        if ((charaProp | BluetoothGattCharacteristic.PROPERTY_READ) > 0) {
            BluetoothLeService.readCharacteristic(characteristic);
        }
    }

    /**
     * Preparing Broadcast receiver to broadcast notify characteristics
     *
     * @param characteristic
     */
    void prepareBroadcastDataNotify(
            BluetoothGattCharacteristic characteristic) {
        final int charaProp = characteristic.getProperties();
        if ((charaProp | BluetoothGattCharacteristic.PROPERTY_NOTIFY) > 0) {
            BluetoothLeService.setCharacteristicNotification(characteristic, true);
        }

    }

    /**
     * Stopping Broadcast receiver to broadcast notify characteristics
     *
     * @param characteristic
     */
    void stopBroadcastDataNotify(
            BluetoothGattCharacteristic characteristic) {
        final int charaProp = characteristic.getProperties();
        if ((charaProp | BluetoothGattCharacteristic.PROPERTY_NOTIFY) > 0) {
            BluetoothLeService.setCharacteristicNotification(characteristic, false);
        }
    }

    /**
     * Preparing Broadcast receiver to broadcast indicate characteristics
     *
     * @param characteristic
     */
    void prepareBroadcastDataIndicate(
            BluetoothGattCharacteristic characteristic) {
        final int charaProp = characteristic.getProperties();

        if ((charaProp | BluetoothGattCharacteristic.PROPERTY_INDICATE) > 0) {
            BluetoothLeService.setCharacteristicIndication(characteristic, true);
        }
    }

    /**
     * Stopping Broadcast receiver to broadcast indicate characteristics
     *
     * @param characteristic
     */
    void stopBroadcastDataIndicate(
            BluetoothGattCharacteristic characteristic) {
        final int charaProp = characteristic.getProperties();

        if ((charaProp | BluetoothGattCharacteristic.PROPERTY_INDICATE) > 0) {
            BluetoothLeService.setCharacteristicIndication(characteristic, false);
        }

    }


    private void writeCharacteristic(BluetoothGattCharacteristic characteristic, byte[] bytes) {
        // Writing the hexValue to the characteristics
        try {
            BluetoothLeService.writeCharacteristicGattDb(characteristic,
                    bytes);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }


    private void showDialog(String info) {

        final MaterialDialog dialog = new MaterialDialog(this);
        dialog.setTitle(getString(R.string.alert))
                .setMessage(info)
                .setPositiveButton(R.string.ok, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
        dialog.show();
    }


    private void stopNotifyOrIndicate() {
        if (nofityEnable)
            stopBroadcastDataNotify(notifyCharacteristic);
        if (indicateEnable)
            stopBroadcastDataIndicate(indicateCharacteristic);
    }


    @Override
    public void onBackPressed() {
        if (optionsMenuManager.getOptionsMenu() != null) {
            dismissMenu();
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopNotifyOrIndicate();
        unregisterReceiver(mGattUpdateReceiver);
    }
}
