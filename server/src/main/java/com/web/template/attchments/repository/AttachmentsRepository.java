package com.web.template.attchments.repository;

import com.web.template.attchments.domain.Attachments;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttachmentsRepository extends JpaRepository<Attachments, Long> {

    Attachments findFirstByFakename(String fakename);
}
