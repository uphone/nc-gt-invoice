package nc.greentown.z90111;

import java.util.ArrayList;
import java.util.List;

import nc.rino.DMOUtil;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;

public class EventHandler2 extends EventHandler {

	public EventHandler2(BillManageUI billUI, IControllerBase control) {
		super(billUI, control);
	}

	protected void onBoQuery() throws Exception {
		StringBuffer strWhere = new StringBuffer();
		if (!askForQueryCondition(strWhere)) {
			return;
		}
		getBufferData().clear();
		List<InvoiceVO> allDataList = new ArrayList<InvoiceVO>();
		Integer total = (Integer) DMOUtil
				.getObject("select count(1) as TOTAL_ from rino_invoice where "
						+ strWhere);
		int pageSize = 10000;
		String fields = "PK_INVOICE,INVTYPE,SYSNO,INVNO,INVDATE,INVAMOUNT,INVAMOUNT_CN,PARTYB,INVMEMO,FEETYPE,PK_CORP,PK_USER,MEMO,PRINTCOUNT,VDEF1,VDEF2,VDEF3,DDEF1,DDEF2,DDEF3,BDEF1,BDEF2,BDEF3,NDEF1,NDEF2,NDEF3,TS,DR,RECHECKUSER,RECHECKTIME,RECHECKFLAG,SEALFLAG,SEALUSER,SEALTIME";
		int pageCount = total % pageSize == 0 ? total / pageSize : (total
				/ pageSize + 1);
		for (int i = 0; i < pageCount; i++) {
			String sql = new StringBuilder().append("select ").append(fields)
					.append(" from (").append("select ").append(fields)
					.append(", ROWNUM as ROWNUM_ from (").append("select ")
					.append(fields)
					.append(" from rino_invoice where nvl(dr,0)=0 and ")
					.append(strWhere)
					.append(" order by invdate,invno,partyb asc) T")
					.append(" ) rino_invoice where ROWNUM_ between ")
					.append(i * pageSize + 1).append(" and ")
					.append((i + 1) * pageSize).toString();
			List<InvoiceVO> voList = (List<InvoiceVO>) DMOUtil.getBeanList(sql,
					InvoiceVO.class);
			allDataList.addAll(voList);
		}
		InvoiceVO[] vos = allDataList
				.toArray(new InvoiceVO[allDataList.size()]);
		addDataToBuffer(vos);
		updateBuffer();
	}
}
