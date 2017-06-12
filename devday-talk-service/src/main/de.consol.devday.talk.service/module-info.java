module de.consol.devday.talk.service {

    requires de.consol.devday.service;

    provides de.consol.devday.service.EventService
        with de.consol.devday.talk.service.TalkService;
}