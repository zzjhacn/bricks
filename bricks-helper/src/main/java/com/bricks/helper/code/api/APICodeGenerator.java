package com.bricks.helper.code.api;

import org.apache.velocity.VelocityContext;

import com.bricks.helper.code.CodeGenResp;
import com.bricks.helper.code.CodeGenerator;
import com.bricks.helper.code.api.domain.APICodeGenReq;
import com.bricks.utils.file.FileUtil;
import com.bricks.utils.velocity.VelocityUtil;

/**
 * @author bricks <long1795@gmail.com>
 */
public class APICodeGenerator implements CodeGenerator<APICodeGenReq> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bricks.helper.code.CodeGenerator#generate(com.bricks.helper.code.CodeGenReq)
	 */
	@Override
	public CodeGenResp generate(APICodeGenReq request) {
		final String tpath = request.codeType().getTemplatePath();
		final String opath = request.outputPath();
		CodeGenResp resp = new CodeGenResp();
		resp.setSucc(true);
		try {
			request.getApis().forEach(a -> {
				VelocityContext vc = new VelocityContext();
				vc.put("req", a);
				String path = FileUtil.parsePath(FileUtil.parsePath(opath) + a.getPkg());
				FileUtil.mkdir(path);
				VelocityUtil.write(vc, tpath + "template.java", path + a.getClazzName() + ".java");
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
