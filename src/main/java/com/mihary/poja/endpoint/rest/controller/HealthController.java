package com.mihary.poja.endpoint.rest.controller;

import static java.util.UUID.randomUUID;

import com.mihary.poja.PojaGenerated;
import com.mihary.poja.endpoint.event.EventProducer;
import com.mihary.poja.endpoint.event.gen.UuidCreated;
import com.mihary.poja.repository.DummyRepository;
import com.mihary.poja.repository.DummyUuidRepository;
import com.mihary.poja.repository.model.Dummy;
import com.mihary.poja.repository.model.DummyUuid;
import java.util.List;
import lombok.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@PojaGenerated
@RestController
@Value
public class HealthController {

  DummyRepository dummyRepository;
  DummyUuidRepository dummyUuidRepository;
  EventProducer eventProducer;

  @GetMapping("/ping")
  public String ping() {
    return "pong";
  }

  @GetMapping("/dummy-table")
  public List<Dummy> dummyTable() {
    return dummyRepository.findAll();
  }

  @GetMapping(value = "/uuid-created")
  public String uuidCreated() throws InterruptedException {
    var randomUuid = randomUUID().toString();
    var event = new UuidCreated().toBuilder().uuid(randomUuid).build();

    eventProducer.accept(List.of(event));

    Thread.sleep(20_000);
    return dummyUuidRepository.findById(randomUuid).map(DummyUuid::getId).orElseThrow();
  }
}
