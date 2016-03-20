package com.bricks.helper.code.webapp;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import org.apache.velocity.VelocityContext;

import com.bricks.helper.code.CodeGenResp;
import com.bricks.helper.code.CodeGenerator;
import com.bricks.helper.code.webapp.domain.App;
import com.bricks.helper.code.webapp.domain.Domain;
import com.bricks.utils.file.FileUtil;
import com.bricks.utils.velocity.VelocityUtil;

/**
 * @author bricks <long1795@gmail.com>
 */
public class WebappCodeGenerator implements CodeGenerator<App> {

	static final String PATH_JAVA = "src/main/java/";
	static final String PATH_RESOURCES = "src/main/resources/";
	static final String PATH_WEBAPP = "src/main/webapp/WEB-INF/";
	static final String PATH_VIEW = "src/main/webapp/WEB-INF/view/";
	static final String PATH_STATICS = "src/main/webapp/statics/";
	static final String PATH_TEST_JAVA = "src/test/java/";
	static final String PATH_TEST_RESOURCES = "src/test/resources/";

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bricks.helper.code.CodeGenerator#generate(com.bricks.helper.code.CodeGenReq)
	 */
	@Override
	public CodeGenResp generate(App request) {
		CodeGenResp resp = new CodeGenResp();
		try {
			prepare(request);
			request.getDomains().forEach(d -> {
				File templateRoot = new File(request.codeType().getTemplatePath());
				VelocityContext vc = buildDomainVc(request, d);
				Arrays.asList(templateRoot.listFiles(FileUtil.simpleFileFilter(".java"))).forEach(f -> {
					VelocityUtil.write(vc, f.getPath(), request.outputPath() + PATH_JAVA + "com/" + request.getAppName() + "/");
				});
			});

			resp.setSucc(true);
			resp.setFilePath(request.outputPath());
			return resp;
		} catch (Exception e) {
			resp.setSucc(false);
			resp.setErrmsg(e.getMessage());
			return resp;
		}
	}

	void prepare(App app) throws IOException {
		String root = app.outputPath();
		FileUtil.mkdir(root + PATH_JAVA);
		FileUtil.mkdir(root + PATH_RESOURCES);
		FileUtil.mkdir(root + PATH_WEBAPP);
		FileUtil.mkdir(root + PATH_VIEW);
		FileUtil.mkdir(root + PATH_STATICS);
		FileUtil.mkdir(root + PATH_TEST_JAVA);
		FileUtil.mkdir(root + PATH_TEST_RESOURCES);

		FileUtil.copy(app.codeType().getTemplatePath() + "statics", root + PATH_STATICS);
	}

	VelocityContext buildDomainVc(App app, Domain d) {
		VelocityContext vc = new VelocityContext();
		vc.put("app", app);
		vc.put("d", d);
		return vc;
	}

}
