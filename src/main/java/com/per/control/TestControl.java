package com.per.control;


import com.per.service.TestService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@RestController
@RequestMapping("/test")
@Api("旁路底库API")
@Slf4j
public class TestControl {

  @Resource(name = "viidAdapterRestTemplate")
  private RestTemplate viidAdapterRestTemplate;
  @Autowired
  private TestService testService;

  @ApiOperation(value = "而是")
  @RequestMapping(value = "/test", method = RequestMethod.POST, produces = "application/json")
  public void test(@RequestParam String faceId, HttpServletResponse response)
      throws IOException {

    response.setHeader("Content-Type", "application/json");
    response.setStatus(200);
    response.setCharacterEncoding(StandardCharsets.UTF_8.name());
//    response.setHeader("");

    PrintWriter writer = response.getWriter();
//    ResponseEntity responseEntity = new ResponseEntity(response, gloablHttpHeaders, HttpStatus.OK);
    try {
      writer.write("你好吗");
    } catch (Exception e) {
//      writer.write(responseEntity.getBody().toString());
    }

    writer.flush();
    writer.close();
  }

  public static Map allHeaders() {
    HttpServletRequest request =
        ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

    Map headers = new HashMap();
    Enumeration headerNames = request.getHeaderNames();
    while (headerNames.hasMoreElements()) {
      String headerName = (String) headerNames.nextElement();

      String headerValue = request.getHeader(headerName);
      headers.put(headerName, headerValue);
    }
    return headers;
  }


  @PostMapping(value = "/testRest", consumes = "application/VIID+JSON;charset=UTF-8")
  public void deal(@RequestBody  AlarmSendRequest alarmSendRequest) {
    ResponseEntity<Object> responseEntity = viidAdapterRestTemplate
        .postForEntity("http://127.0.0.1:8081/test/VIID/DispositionNotifications", alarmSendRequest,
            Object.class);

  }

  @PostMapping(value = "/VIID/DispositionNotifications", consumes = "application/VIID+JSON;charset=UTF-8")
  public void dealTest(@RequestBody String alarmSendRequest) {
    log.info("alarmSendRequest={}", alarmSendRequest);
    testService.testAdd();
  }

}