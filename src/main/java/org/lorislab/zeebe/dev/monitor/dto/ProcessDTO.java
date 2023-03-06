package org.lorislab.zeebe.dev.monitor.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;

import java.util.List;

@RegisterForReflection
public record ProcessDTO(ProcessInfoDTO info, String xml, List<InstanceTableItemDTO> instances,
                         List<TimerDTO> timers, List<MessageSubscriptionDTO> messageSubscriptions,
                         ProcessInstanceDTO instance) {};
