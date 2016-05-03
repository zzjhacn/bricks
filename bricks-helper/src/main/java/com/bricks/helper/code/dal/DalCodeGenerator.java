package com.bricks.helper.code.dal;

import org.apache.velocity.VelocityContext;

import com.bricks.helper.code.CodeGenResp;
import com.bricks.helper.code.CodeGenerator;
import com.bricks.helper.code.dal.domain.DBCodeGenReq;
import com.bricks.utils.file.FileUtil;
import com.bricks.utils.velocity.VelocityUtil;

/**
 * @author bricks <long1795@gmail.com>
 */
public class DalCodeGenerator implements CodeGenerator<DBCodeGenReq> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bricks.helper.code.CodeGenerator#generate(com.bricks.helper.code.CodeGenReq)
	 */
	@Override
	public CodeGenResp generate(DBCodeGenReq request) {
		final String tpath = request.codeType().getTemplatePath();
		final String opath = request.outputPath();
		CodeGenResp resp = new CodeGenResp();
		resp.setSucc(true);
		try {
			request.getTables().forEach(t -> {
				VelocityContext vc = new VelocityContext();
				vc.put("req", t);
				String path = FileUtil.parsePath(FileUtil.parsePath(opath) + t.getPkg());
				FileUtil.mkdir(path);
				VelocityUtil.write(vc, tpath + "AbsTemplateEO.java", path + "Abs" + t.getClazzName() + ".java");
				VelocityUtil.write(vc, tpath + "TemplateEO.java", path + t.getClazzName() + ".java");
				VelocityUtil.write(vc, tpath + "TemplateDao.java", path + t.getClazzName() + "Dao.java");
			});
			resp.setFilePath(opath);
		} catch (Throwable t) {
			resp.setErrmsg(t.getMessage());
			resp.setSucc(false);
			err(t);
		}
		return resp;
	}

}
