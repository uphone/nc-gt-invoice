package nc.greentown.z90110;

import nc.ui.trade.business.HYPubBO_Client;
import nc.vo.org.OrgVO;
import nc.vo.pub.SuperVO;
import nc.vo.pubapp.AppContext;

public class CorpTreeData implements nc.ui.trade.pub.IVOTreeDataByID {
	private String[] pk_orgs;

	public CorpTreeData(String[] pk_orgs) {
		this.pk_orgs = pk_orgs;
	}

	public String getShowFieldName() {
		return "code+name";
	}

	public SuperVO[] getTreeVO() {
		try {
			String pk_group = AppContext.getInstance().getPkGroup();
			String orgFileterCondition = getOrgFilterCondition();
			return HYPubBO_Client.queryByCondition(OrgVO.class,
					" isbusinessunit='Y' and enablestate='2' and pk_group='"
							+ pk_group + "'" + orgFileterCondition
							+ " order by code");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public String getOrgFilterCondition() {
		String pk_org_condition = "";
		if (pk_orgs != null) {
			String bb = getInStr(pk_orgs);
			pk_org_condition = " and " + bb;
		}
		return pk_org_condition;
	}

	public String getIDFieldName() {
		return "pk_org";
	}

	public String getParentIDFieldName() {
		return "pk_fatherorg";
	}

	private String getInStr(String[] ids) {
		int len = ids.length;
		int count = 1000;
		int size = len % count;
		if (size == 0) {
			size = len / count;
		} else {
			size = (len / count) + 1;
		}
		StringBuilder ret = new StringBuilder();
		for (int i = 0; i < size; i++) {
			if (i != 0) {
				ret.append(" or pk_org in (");
			} else {
				ret.append("( pk_org in (");
			}
			int fromIndex = i * count;
			int toIndex = Math.min(fromIndex + count, len);
			for (int m = fromIndex; m < toIndex; m++) {
				ret.append("'").append(ids[m]).append("',");
			}
			ret = ret.deleteCharAt(ret.length() - 1);
			ret.append(")");
		}
		ret.append(")");
		return ret.toString();
	}
}
