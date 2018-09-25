package io.nichijou.oops.simple

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.github.florent37.glidepalette.GlidePalette
import io.nichijou.oops.Oops
import io.nichijou.oops.OopsActivity
import io.nichijou.oops.ext.insetStatusBar
import io.nichijou.oops.ext.isColorLight
import io.nichijou.oops.ext.setLightStatusBarCompat
import io.nichijou.oops.ext.translucentStatusBar
import io.nichijou.oops.widget.BottomNavigationViewBackgroundMode
import io.nichijou.oops.widget.BottomNavigationViewIconTextMode
import kotlinx.android.synthetic.main.activity_secondary.*
import java.util.*

class SecondaryActivity : OopsActivity() {
    private val url = arrayOf(
            "http://fmn.rrimg.com/fmn077/xiaozhan/20150527/0920/original_Kl67_8140000107ca1e83.jpg",
            "http://fmn.rrimg.com/fmn078/xiaozhan/20150527/0920/original_goQE_87e7000103101e7f.jpg",
            "http://fmn.rrimg.com/fmn077/xiaozhan/20150527/0920/xlarge_0bGn_8bed0001079e1e84.jpg",
            "http://fmn.rrimg.com/fmn071/xiaozhan/20150527/0920/xlarge_9Y5f_8ca70001079f1e84.jpg",
            "http://fmn.rrimg.com/fmn071/xiaozhan/20150527/0920/original_BOfk_8bc8000107ce1e84.jpg",
            "http://fmn.rrimg.com/fmn072/xiaozhan/20150527/0920/original_kL2j_87c60001031e1e7f.jpg",
            "http://fmn.rrimg.com/fmn073/xiaozhan/20150527/0920/original_zBMj_63d8000107f11e80.jpg",
            "http://fmn.rrimg.com/fmn072/xiaozhan/20150527/0920/xlarge_uGhG_81620001079c1e83.jpg",
            "http://fmn.rrimg.com/fmn077/xiaozhan/20150527/0920/xlarge_WGA9_638b000107ed1e80.jpg",
            "http://fmn.rrimg.com/fmn077/xiaozhan/20150527/0920/xlarge_7rOx_6376000107c21e80.jpg",
            "http://fmn.rrimg.com/fmn070/xiaozhan/20150527/0920/xlarge_mhtx_812a000107ac1e83.jpg",
            "http://fmn.rrimg.com/fmn070/xiaozhan/20150527/0920/original_2HqK_6381000107a71e80.jpg",
            "http://fmn.rrimg.com/fmn077/xiaozhan/20150527/0920/original_OGRq_8829000102f81e7f.jpg",
            "http://fmn.rrimg.com/fmn075/xiaozhan/20150527/0920/original_lsYI_634a000107c31e80.jpg",
            "http://fmn.rrimg.com/fmn077/xiaozhan/20150524/1520/xlarge_hvp2_881400009def1e7f.jpg",
            "http://fmn.rrimg.com/fmn077/xiaozhan/20150524/1520/xlarge_iFDX_8bd60000a0511e84.jpg",
            "http://fmn.rrimg.com/fmn071/xiaozhan/20150524/1520/xlarge_N9Po_87fe00009dcd1e7f.jpg",
            "http://fmn.rrimg.com/fmn074/xiaozhan/20150524/1520/xlarge_fYdh_81830000a0251e83.jpg",
            "http://fmn.rrimg.com/fmn077/xiaozhan/20150524/1520/xlarge_TdTh_63550000a0771e80.jpg",
            "http://fmn.rrimg.com/fmn078/xiaozhan/20150524/1520/original_QQJB_63ef0000a0571e80.jpg",
            "http://fmn.rrimg.com/fmn079/xiaozhan/20150524/1520/xlarge_SwAn_812a0000a05f1e83.jpg",
            "http://fmn.rrimg.com/fmn074/xiaozhan/20150524/1520/original_kAEd_8c180000a04d1e84.jpg",
            "http://fmn.rrimg.com/fmn076/xiaozhan/20150524/1520/original_zQqC_819c0000a0301e83.jpg",
            "http://fmn.rrimg.com/fmn072/xiaozhan/20150517/2040/xlarge_u8l8_1d56000548ad1e84.jpg",
            "http://fmn.rrimg.com/fmn079/xiaozhan/20150517/2040/xlarge_8Hpc_ec8e00052a5f1e80.jpg",
            "http://fmn.rrimg.com/fmn076/xiaozhan/20150517/2040/xlarge_Naxv_94c700051bc31e83.jpg",
            "http://fmn.rrimg.com/fmn074/xiaozhan/20150517/2040/xlarge_gpi5_922500054a9e1e7f.jpg",
            "http://fmn.rrimg.com/fmn076/xiaozhan/20150517/2040/xlarge_kJND_1d6c0005483f1e84.jpg",
            "http://fmn.rrimg.com/fmn076/xiaozhan/20150517/2040/xlarge_3YRy_921a00054adf1e7f.jpg",
            "http://fmn.rrimg.com/fmn077/xiaozhan/20150517/2040/xlarge_pSPe_1cfd0005478b1e84.jpg",
            "http://fmn.rrimg.com/fmn072/xiaozhan/20150503/2115/original_3bBz_94e90002fad81e83.jpg",
            "http://fmn.rrimg.com/fmn070/xiaozhan/20150503/2115/original_67Bv_1d560002ffa91e84.jpg",
            "http://fmn.rrimg.com/fmn070/xiaozhan/20150503/2115/original_67Bv_94ff0002faea1e83.jpg",
            "http://fmn.rrimg.com/fmn072/xiaozhan/20150503/2115/original_4wWc_925d0003048f1e7f.jpg",
            "http://fmn.rrimg.com/fmn071/xiaozhan/20150503/2115/original_lSaJ_1d090002ff821e84.jpg",
            "http://fmn.rrimg.com/fmn077/xiaozhan/20150503/2115/original_gRkF_ec8e0002fb421e80.jpg",
            "http://fmn.rrimg.com/fmn070/xiaozhan/20150502/1340/xlarge_Vq1K_1da30002c1f71e84.jpg",
            "http://fmn.rrimg.com/fmn074/xiaozhan/20150502/1340/xlarge_xBRd_95560002c0861e83.jpg",
            "http://fmn.rrimg.com/fmn077/xiaozhan/20150502/1340/xlarge_MGHc_92730002c8621e7f.jpg",
            "http://fmn.rrimg.com/fmn071/xiaozhan/20150502/1340/xlarge_JfkS_92250002c8851e7f.jpg",
            "http://fmn.rrimg.com/fmn070/xiaozhan/20150502/1340/xlarge_hPAv_ece60002c0581e80.jpg",
            "http://fmn.rrimg.com/fmn070/xiaozhan/20150502/1340/xlarge_hPAv_ece60002c0581e80.jpg",
            "http://fmn.rrimg.com/fmn074/xiaozhan/20150502/1340/original_qH9z_1d560002c23f1e84.jpg",
            "http://fmn.rrimg.com/fmn073/xiaozhan/20150501/1020/original_TYvr_ec09000291011e80.jpg",
            "http://fmn.rrimg.com/fmn076/xiaozhan/20150501/1020/xlarge_TiI7_9556000291161e83.jpg",
            "http://fmn.rrimg.com/fmn075/xiaozhan/20150501/1020/xlarge_T7UJ_9520000290bc1e83.jpg",
            "http://fmn.rrimg.com/fmn079/xiaozhan/20150501/1020/original_Bv92_91880002988a1e7f.jpg",
            "http://fmn.rrimg.com/fmn070/xiaozhan/20150501/1020/original_s8fF_92730002987c1e7f.jpg",
            "http://fmn.rrimg.com/fmn072/xiaozhan/20150501/1020/original_Jtuz_91b6000298a31e7f.jpg",
            "http://fmn.rrimg.com/fmn070/xiaozhan/20150501/1020/original_Syoh_9515000290651e83.jpg",
            "http://fmn.rrimg.com/fmn077/xiaozhan/20150501/1020/original_GabN_ec4c000290bf1e80.jpg",
            "http://fmn.rrimg.com/fmn076/xiaozhan/20150501/1020/original_Ij1c_1dae000290d11e84.jpg",
            "http://fmn.rrimg.com/fmn071/xiaozhan/20150501/1020/original_zdtK_ec41000290ab1e80.jpg",
            "http://fmn.rrimg.com/fmn079/xiaozhan/20150501/1020/original_cCTg_1cbc000290821e84.jpg",
            "http://fmn.rrimg.com/fmn079/xiaozhan/20150501/1020/original_hEql_94bc000290dc1e83.jpg",
            "http://fmn.rrimg.com/fmn076/xiaozhan/20150501/1020/xlarge_BAKo_920f000298ca1e7f.jpg",
            "http://fmn.rrimg.com/fmn061/xiaozhan/20150207/2220/xlarge_mPdX_341b000208f5125d.jpg",
            "http://fmn.rrimg.com/fmn065/xiaozhan/20150207/2220/xlarge_2o8f_335500020949125d.jpg",
            "http://fmn.rrimg.com/fmn064/xiaozhan/20150207/2220/xlarge_xzs9_22c800004b3f1190.jpg",
            "http://fmn.rrimg.com/fmn057/xiaozhan/20150207/2220/xlarge_eg1D_41ac00006040118c.jpg",
            "http://fmn.rrimg.com/fmn058/xiaozhan/20150207/2220/xlarge_6BL2_41e3000060d4118c.jpg",
            "http://fmn.rrimg.com/fmn064/xiaozhan/20150207/2220/xlarge_nvYM_3376000209a6125d.jpg",
            "http://fmn.rrimg.com/fmn062/xiaozhan/20150207/2220/xlarge_AiwX_3334000208c5125d.jpg",
            "http://fmn.rrimg.com/fmn056/xiaozhan/20150207/2220/xlarge_qk9z_3af800003576118f.jpg",
            "http://fmn.rrimg.com/fmn058/xiaozhan/20150207/2220/xlarge_DUXa_234200004b3b1190.jpg",
            "http://fmn.rrimg.com/fmn062/xiaozhan/20150207/2220/xlarge_L7vj_34050002090b125d.jpg",
            "http://fmn.rrimg.com/fmn062/xiaozhan/20150207/2220/xlarge_L7vj_34050002090b125d.jpg",
            "http://fmn.rrimg.com/fmn056/xiaozhan/20150207/2220/xlarge_ks8i_7bf300003754125f.jpg",
            "http://fmn.rrimg.com/fmn059/xiaozhan/20150101/1045/original_cd66_37000001c2f2125f.jpg",
            "http://fmn.rrimg.com/fmn057/xiaozhan/20141230/1525/original_Ythj_4c6800026670125d.jpg",
            "http://fmn.rrimg.com/fmn064/xiaozhan/20141230/1525/xlarge_D1vj_0aa8000136181191.jpg",
            "http://fmn.rrimg.com/fmn060/xiaozhan/20141230/1525/original_oKRV_3690000192b6125f.jpg",
            "http://fmn.rrimg.com/fmn060/xiaozhan/20141230/1525/xlarge_Bjp2_14cc000133c01190.jpg",
            "http://fmn.rrimg.com/fmn062/xiaozhan/20141230/1525/original_pnC3_4832000092cc118f.jpg",
            "http://fmn.rrimg.com/fmn065/xiaozhan/20141230/1525/original_fAuj_361b0001929b125f.jpg",
            "http://fmn.rrimg.com/fmn064/xiaozhan/20141228/0935/original_RrkO_148b0000fd1d1190.jpg",
            "http://fmn.rrimg.com/fmn056/xiaozhan/20141226/2245/original_XjFK_39540000b286118c.jpg",
            "http://fmn.rrimg.com/fmn065/xiaozhan/20141226/2245/original_QYSh_13630000de111190.jpg",
            "http://fmn.rrimg.com/fmn063/xiaozhan/20141226/2245/xlarge_GURL_485e00003cb4118f.jpg",
            "http://fmn.rrimg.com/fmn062/xiaozhan/20141226/2245/original_PB1J_0b780000e0541191.jpg",
            "http://fmn.rrimg.com/fmn064/xiaozhan/20141225/0850/original_preD_490d0000124c118f.jpg",
            "http://fmn.rrimg.com/fmn061/xiaozhan/20141225/0850/original_23Sv_148b0000b3d61190.jpg",
            "http://fmn.rrimg.com/fmn063/xiaozhan/20141225/0850/xlarge_quOV_4ba80001ecb8125d.jpg",
            "http://fmn.rrimg.com/fmn061/xiaozhan/20141225/0850/xlarge_eOro_0b780000b5f21191.jpg",
            "http://fmn.rrimg.com/fmn060/xiaozhan/20141225/0850/original_4PY8_39140000679a118c.jpg",
            "http://fmn.rrimg.com/fmn057/xiaozhan/20141225/0850/original_1r8k_36a000010af1125f.jpg",
            "http://fmn.rrimg.com/fmn057/xiaozhan/20141225/0850/original_YclT_3924000067a2118c.jpg",
            "http://fmn.rrimg.com/fmn060/xiaozhan/20141225/0850/xlarge_TJB7_143b0000b3c91190.jpg",
            "http://fmn.rrimg.com/fmn063/xiaozhan/20141225/0850/original_7w9j_3867000067c6118c.jpg",
            "http://fmn.rrimg.com/fmn064/xiaozhan/20141225/0850/original_0rdy_0b080000b6101191.jpg",
            "http://fmn.rrimg.com/fmn058/xiaozhan/20141225/0850/original_VpX9_379000010aa7125f.jpg",
            "http://fmn.rrimg.com/fmn063/xiaozhan/20141223/1430/original_JsRa_4c680001c5b3125d.jpg",
            "http://fmn.rrimg.com/fmn064/xiaozhan/20141223/1430/original_MRhw_36c00000dddd125f.jpg",
            "http://fmn.rrimg.com/fmn064/xiaozhan/20141223/1430/original_iS01_020e0000e12d118f.jpg",
            "http://fmn.rrimg.com/fmn057/xiaozhan/20141223/1430/xlarge_lq4U_0a3800008bdb1191.jpg",
            "http://fmn.rrimg.com/fmn058/xiaozhan/20141223/1425/original_7Cnk_37800000de49125f.jpg",
            "http://fmn.rrimg.com/fmn064/xiaozhan/20141223/1425/xlarge_O0X3_394400001e00118c.jpg",
            "http://fmn.rrimg.com/fmn058/xiaozhan/20141223/1430/original_DZuh_36300000de55125f.jpg",
            "http://fmn.rrimg.com/fmn063/xiaozhan/20141223/1425/original_vjOf_4be80001c54c125d.jpg",
            "http://fmn.rrimg.com/fmn057/xiaozhan/20141223/1430/xlarge_sj8L_4bb80001c595125d.jpg",
            "http://fmn.rrimg.com/fmn057/xiaozhan/20141223/1430/xlarge_vzDV_0a0300008bb91191.jpg",
            "http://fmn.rrimg.com/fmn062/xiaozhan/20141210/1515/xlarge_nIgb_01850000a1c2118f.jpg",
            "http://fmn.rrimg.com/fmn061/xiaozhan/20141210/1515/original_2PuI_059d0000a1d61190.jpg",
            "http://fmn.rrimg.com/fmn063/xiaozhan/20141210/1515/original_fzNO_134c000116d6118c.jpg",
            "http://fmn.rrimg.com/fmn059/xiaozhan/20141210/1515/xlarge_6XsU_1406000116e6118c.jpg",
            "http://fmn.rrimg.com/fmn060/xiaozhan/20141207/2120/xlarge_dzov_4ba8000054c1125d.jpg",
            "http://fmn.rrimg.com/fmn061/xiaozhan/20141207/2120/xlarge_K1iF_130c00009d10118c.jpg",
            "http://fmn.rrimg.com/fmn060/xiaozhan/20141207/2120/original_K1wG_4bf8000054c4125d.jpg",
            "http://fmn.rrimg.com/fmn058/xiaozhan/20141207/2120/xlarge_pqgi_05fe00005b191190.jpg",
            "http://fmn.rrimg.com/fmn057/xiaozhan/20141207/2120/original_1EyK_404200005b121191.jpg",
            "http://fmn.rrimg.com/fmn061/xiaozhan/20141207/2120/original_YN3v_069e00005b151190.jpg",
            "http://fmn.rrimg.com/fmn060/xiaozhan/20141207/2120/original_fG03_021f00005aff118f.jpg",
            "http://fmn.rrimg.com/fmn061/xiaozhan/20141207/2120/original_JWK2_131c00009d6e118c.jpg",
            "http://fmn.rrimg.com/fmn061/xiaozhan/20141207/2120/xlarge_GOxM_409200005b1f1191.jpg",
            "http://fmn.rrimg.com/fmn062/xiaozhan/20141201/0955/xlarge_wDov_18ca00009a4e118f.jpg",
            "http://fmn.rrimg.com/fmn056/xiaozhan/20141201/0955/xlarge_JSBp_18ba00009a0f118f.jpg",
            "http://fmn.rrimg.com/fmn059/xiaozhan/20141201/0955/xlarge_ewF8_04f30000e8501191.jpg",
            "http://fmn.rrimg.com/fmn058/xiaozhan/20141201/0955/xlarge_T6t9_685900008466125f.jpg",
            "http://fmn.rrimg.com/fmn057/xiaozhan/20141201/0955/xlarge_ZdoS_2a66000093bb118c.jpg",
            "http://fmn.rrimg.com/fmn064/xiaozhan/20141201/0955/original_HbrM_68f4000083c7125f.jpg",
            "http://fmn.rrimg.com/fmn064/xiaozhan/20141201/0955/xlarge_FF4f_05290000e8581191.jpg",
            "http://fmn.rrimg.com/fmn060/xiaozhan/20141201/0955/xlarge_PLGY_1f100000c4ad1190.jpg",
            "http://fmn.rrimg.com/fmn057/xiaozhan/20141201/0955/xlarge_ccib_1e560000c5221190.jpg",
            "http://fmn.rrimg.com/fmn062/xiaozhan/20141201/0955/original_Wddt_690500006ff7125d.jpg",
            "http://fmn.rrimg.com/fmn062/xiaozhan/20141201/0955/original_Wddt_690500006ff7125d.jpg",
            "http://fmn.rrimg.com/fmn064/xiaozhan/20141201/0955/xlarge_fM60_05ab0000e8761191.jpg",
            "http://fmn.rrimg.com/fmn056/xiaozhan/20141201/0955/xlarge_HZS5_1f310000c5011190.jpg",
            "http://fmn.rrimg.com/fmn059/xiaozhan/20141201/0955/xlarge_aKgS_2984000093b2118c.jpg",
            "http://fmn.rrimg.com/fmn057/xiaozhan/20141201/0955/xlarge_wuAJ_69b500007046125d.jpg",
            "http://fmn.rrimg.com/fmn063/xiaozhan/20141201/0955/original_UozR_2a86000093be118c.jpg",
            "http://fmn.rrimg.com/fmn059/xiaozhan/20141130/2240/original_XwfG_69b500006ac3125d.jpg",
            "http://fmn.rrimg.com/fmn065/xiaozhan/20141130/2240/xlarge_L5V2_68d400007d94125f.jpg",
            "http://fmn.rrimg.com/fmn062/xiaozhan/20141130/2240/original_6x0X_29d6000088ed118c.jpg",
            "http://fmn.rrimg.com/fmn065/xiaozhan/20141130/2240/original_xFIJ_1ef00000becd1190.jpg",
            "http://fmn.rrimg.com/fmn059/xiaozhan/20141121/2245/xlarge_MKyc_46f60002092b1191.jpg",
            "http://fmn.rrimg.com/fmn056/xiaozhan/20141116/1355/xlarge_RV3H_47a600017dfb1191.jpg",
            "http://fmn.rrimg.com/fmn062/xiaozhan/20141026/2230/xlarge_OFIr_14d0000253cd118c.jpg",
            "http://fmn.rrimg.com/fmn057/xiaozhan/20141026/2230/original_nRzV_14b00002524a118c.jpg",
            "http://fmn.rrimg.com/fmn057/xiaozhan/20141026/2230/original_8Wvw_2394000074f8125f.jpg",
            "http://fmn.rrimg.com/fmn060/xiaozhan/20141012/1155/xlarge_yb1i_4563000291ac118c.jpg",
            "http://fmn.rrimg.com/fmn056/xiaozhan/20141012/1155/xlarge_4saK_4bef000187c0118f.jpg",
            "http://fmn.rrimg.com/fmn057/xiaozhan/20141012/1150/original_VPAp_513d000042dc125d.jpg",
            "http://fmn.rrimg.com/fmn059/xiaozhan/20141012/1150/xlarge_pIbw_0e3a000053de1190.jpg",
            "http://fmn.rrimg.com/fmn058/xiaozhan/20141012/1155/xlarge_5VLB_35d0000187531190.jpg",
            "http://fmn.rrimg.com/fmn062/xiaozhan/20141012/1150/xlarge_mk1i_4c7100018741118f.jpg",
            "http://fmn.rrimg.com/fmn058/xiaozhan/20141012/1150/xlarge_rr6V_454300029185118c.jpg",
            "http://fmn.rrimg.com/fmn058/xiaozhan/20141012/1150/xlarge_rr6V_454300029185118c.jpg",
            "http://fmn.rrimg.com/fmn060/xiaozhan/20141012/1150/xlarge_WSCf_3666000187151190.jpg",
            "http://fmn.rrimg.com/fmn062/xiaozhan/20141012/1155/xlarge_CKtc_35800001874c1190.jpg",
            "http://fmn.rrimg.com/fmn062/xiaozhan/20140929/0810/original_dEWj_512d00005d93118f.jpg",
            "http://fmn.rrimg.com/fmn060/xiaozhan/20140929/0810/original_K4bx_7b9b00005dc41190.jpg",
            "http://fmn.rrimg.com/fmn060/xiaozhan/20140929/0810/original_s1L8_7baa00005d911190.jpg",
            "http://fmn.rrimg.com/fmn060/xiaozhan/20140929/0810/original_17vy_6ed50001827d1191.jpg",
            "http://fmn.rrimg.com/fmn058/xiaozhan/20140929/0810/original_8ap0_6e890001b41d125f.jpg",
            "http://fmn.rrimg.com/fmn064/xiaozhan/20140929/0810/xlarge_nG3E_5c8300014fdd125d.jpg"
    )
    private val bgArr = arrayOf(BottomNavigationViewBackgroundMode.AUTO, BottomNavigationViewBackgroundMode.ACCENT, BottomNavigationViewBackgroundMode.PRIMARY, BottomNavigationViewBackgroundMode.PRIMARY_DARK)
    private val itArr = arrayOf(BottomNavigationViewIconTextMode.AUTO, BottomNavigationViewIconTextMode.ACCENT, BottomNavigationViewIconTextMode.PRIMARY)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_secondary)
        setOverStatusBarColor(Color.TRANSPARENT)
        insetStatusBar()
        translucentStatusBar()
        if (Build.VERSION_CODES.KITKAT <= Build.VERSION.SDK_INT) {
            toolbar.topStatusBarMargin()
        }
        collapsingToolbar.title = "Secondary"
        toolbar.inflateMenu(R.menu.menu_secondary)
        toolbar.menu.findItem(R.id.action_change_image).setOnMenuItemClickListener {
            loadImage()
            true
        }
        toolbar.setNavigationOnClickListener { onBackPressed() }
        fab.setOnClickListener {
            changeTheme(it)
            loadImage()
        }
        loadImage()
    }

    private fun changeTheme(view: View) {
        val primary = randomColor()
        Oops.oops {
            colorAccent = randomColor()
            colorPrimary = primary
            statusBarColor = primary
            navBarColor = primary
            iconTitleActiveColor = randomColor()
            bottomNavigationViewBackgroundMode = bgArr[Random().nextInt(bgArr.size)]
            bottomNavigationViewIconTextMode = itArr[Random().nextInt(itArr.size)]
            swipeRefreshLayoutBackgroundColor = randomColor()
            swipeRefreshLayoutSchemeColor = randomColors()
            isDark = false
            rippleAnimDuration = 480
            rippleView = view
        }
    }

    private fun loadImage() {
        val u = url[Random().nextInt(url.size)]
        Glide.with(this)
                .load(u)
                .listener(GlidePalette.with(u)
                        .intoCallBack { p ->
                            p?.dominantSwatch?.rgb?.let {
                                Oops.oops.collapsingToolbarColor = it
                                setLightStatusBarCompat(it.isColorLight())
                            }
                        }
                )
                .into(image_view)
    }
}
