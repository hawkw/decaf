Binky.____Method:
	BeginFunc 0 ;
	EndFunc ;
VTable Binky =
	Binky.____Method,
; 
main:
	BeginFunc 52 ;
	_tmp0 = 0 ;
	_tmp1 = 4 ;
	_tmp2 = _tmp1 + _tmp0 ;
	PushParam _tmp2 ;
	_tmp3 = LCall _Alloc ;
	PopParams 4 ;
	_tmp4 = Binky ;
	*(_tmp3) = _tmp4 ;
	d = _tmp3 ;
	_tmp5 = 0 ;
	_tmp6 = 4 ;
	_tmp7 = _tmp6 + _tmp5 ;
	PushParam _tmp7 ;
	_tmp8 = LCall _Alloc ;
	PopParams 4 ;
	_tmp9 = Binky ;
	*(_tmp8) = _tmp9 ;
	a = _tmp8 ;
	a = d ;
	EndFunc ;
