__A.fn:
	BeginFunc 16 ;
	y = x ;
	PushParam y ;
	LCall _PrintInt ;
	PopParams 4 ;
	_tmp0 = 4 ;
	_tmp1 = this + _tmp0 ;
	*(_tmp1) = y ;
	_tmp2 = *(this + 4) ;
	PushParam _tmp2 ;
	LCall _PrintInt ;
	PopParams 4 ;
	EndFunc ;
VTable A =
	__A.fn,
; 
main:
	BeginFunc 28 ;
	_tmp3 = 8 ;
	PushParam _tmp3 ;
	_tmp4 = LCall _Alloc ;
	PopParams 4 ;
	_tmp5 = A ;
	*(_tmp4) = _tmp5 ;
	a = _tmp4 ;
	_tmp6 = *(a) ;
	_tmp7 = *(_tmp6) ;
	_tmp8 = 137 ;
	PushParam _tmp8 ;
	PushParam a ;
	ACall _tmp7 ;
	PopParams 8 ;
	EndFunc ;
