package ru.itmo.webmail.model.service;

import ru.itmo.webmail.model.domain.Event;
import ru.itmo.webmail.model.repository.EventRepository;
import ru.itmo.webmail.model.repository.impl.EventRepositoryImpl;

public class EventService {
    private EventRepository eventRepository = new EventRepositoryImpl();

    public void addEvent(Event event) {
        eventRepository.save(event);
    }
}
