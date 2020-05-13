package com.lee.oneweekonebook.Data;

/*
total_count	전체 검색된 문서수	Integer
pageable_count	검색결과로 제공 가능한 문서수	Integer
is_end	현재 페이지가 마지막 페이지인지 여부. 값이 false이면 page를 증가시켜 다음 페이지를 요청할 수 있음	Boolean
* */

public class BookPage {
    public Boolean is_end;
    public int pageable_count;
    public int total_count;

}
