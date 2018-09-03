package com.web.tempalte.attchments.repository;

import com.web.tempalte.attchments.domain.Attachments;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttachmentsRepository extends JpaRepository<Attachments, Long> {

    Attachments findFirstByFakename(String fakename);
}
