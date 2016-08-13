//v20090306-1
//v20090413-2
//v20090531-3
//v20090604-4
package com.ccthanking.framework.coreapp.freequery;

public class FreeQueryProps 
{
	//��¼�������ƣ����ֵ���Ը�����Ҫ�������ô�С���������ط��õ���������Ӱ��
	public static final int ROWS_LIMITED_INT = 10000;

	
	//�������е�ֵ������������Ķ����жദ��������Щ���ݽ��������жϣ��ڴ˴�����Щ���ͽ�����˵��
	
	//FIELD_TYPE��ʾ��fw_user_field�����õ��ֶ�����(style�ֶ�)
	public static final String FIELD_TYPE_CHAR_1 = "1";			//�ַ�
	public static final String FIELD_TYPE_IDCARD_2 = "2";		//���֤��
	public static final String FIELD_TYPE_DIC_3 = "3";			//�ֵ�
	public static final String FIELD_TYPE_DATE_4 = "4";			//����
	public static final String FIELD_TYPE_BLOB_5= "5";			//������

	//
	public static final String FIELD_KIND_CHAR_1 = "1";			//�ַ�
	public static final String FIELD_KIND_DIC_6 = "6";			//�ֵ�
	public static final String FIELD_KIND_CARD_7 = "7";			//
	
	//SET_STYLE��ʾ����ʱ�Ŀ�ѡ��ʽ����
	public static final String SET_STYLE_SET_2 = "2";			//���ϣ���ֵ��
	public static final String SET_STYLE_AGE_3 = "3";			//����
	public static final String SET_STYLE_TYJ_4 = "4";			//ͬ������
	public static final String SET_STYLE_TYQ_5 = "5";			//ͬ����ȫ��
	public static final String SET_STYLE_RANGE_8 = "8";			//����
	public static final String SET_STYLE_TREEDIC_11 = "11";		//�����ֵ�
	public static final String SET_STYLE_TABSEL_12 = "12";		//��ѡ
	public static final String SET_STYLE_1518ID_13 = "13";		//����15λ��18λ�����֤�š�����������һ�����룬�ɲ�ѯ���ָ�ʽ������

	//��ʽ��������Ŀ��
	public static final String FMT_TARGER_NULL_0 = "0";			//��Ӧ��ʽ���ġ��ա�
	public static final String FMT_TARGER_VALUE_1 = "1";		//��Ӧ��ʽ���ġ�ֵ��
	public static final String FMT_TARGER_SOURCE_2 = "2";		//��Ӧ��ʽ���ġ�Դ��

	//FMT_TYPE��ʾ��ʽ������
	public static final String FMT_TYPE_TRUNC_1 = "1";			//��ȡ
	public static final String FMT_TYPE_DATE_2 = "2";			//����
	public static final String FMT_TYPE_CUSTOM_3 = "3";			//�Զ���
	public static final String FMT_TYPE_ODD0_4 = "4";			//ȥ������0
	public static final String FMT_TYPE_EVEN0_5 = "5";			//ȥż����0
	public static final String FMT_TYPE_FULL0_6 = "6";			//ȥȫ��0
	public static final String FMT_TYPE_DIC_7 = "7";			//�ֵ����

	//�̶�ֵ���ĸ�����
	public static final String FIXED_WSZ_0 = "0";				//δ���ã���δѡ��̶�ֵ�����
	public static final String FIXED_YHZH_1 = "1";				//�û��ʻ�
	public static final String FIXED_YHBM_2 = "2";				//�û�����
	public static final String FIXED_XTSJ_3 = "3";				//ϵͳʱ��
	public static final String FIXED_MJJB_4 = "4";				//�ܼ�����

	//�ĸ���������
	public static final String COND_FILTER_1 = "1";				//��������
	public static final String COND_DYNIMIC_2 = "2";			//��̬����
	public static final String COND_RELATION_3 = "3";			//���ù���
	public static final String COND_FIXED_4 = "4";				//�̶�ֵ

	//�ۺϲ�ѯ�������õ��ı����ڸù����ڲ�ͬ�Ŀ����ά��
	public static final String MENU = "eap_menu";							//�˵���Ϣ
	public static final String USER_TABLE = "fw_user_table";				//ҵ�����Ϣ
	public static final String USER_FIELD = "fw_user_field";				//����ֶ���Ϣ
	public static final String FREE_QUERY = "fw_free_query";				//����Ĳ�ѯ��Ϣ
	public static final String FREE_CUSTOM = "fw_free_custom";				//�Զ�����Ϣ
	public static final String FREE_RELATION = "fw_free_relation";			//��������Ϣ
	
	//�ۺϲ�ѯ���ó���
	public static final String COMBINECONDITION_SHOW = "CS";				//����Ϣ���õ�fw_user_field��secret_level�ֶ��У�
																			//��ʾ���ֶ������������ѯ����ʾ
}
