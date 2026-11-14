package com.back;

import org.springframework.data.jpa.repository.JpaRepository;

// 답변(Answer) 엔티티용 리포지터리. QuestionRepository 와 같은 방식이다.
// 상속만으로 save, findById, findAll, count, delete 등을 쓸 수 있다.
// Repository for the Answer entity. Works the same way as QuestionRepository.
// Extending JpaRepository is enough to get save, findById, findAll, count, delete, and so on.
public interface AnswerRepository extends JpaRepository<Answer, Integer> {
}
