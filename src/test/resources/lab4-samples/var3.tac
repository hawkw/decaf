main:
	BeginFunc 28 ;
	_tmp0 = 10 ;
	a = _tmp0 ;
	_tmp1 = 82 ;
	b = _tmp1 ;
	_tmp2 = 5 ;
	b = _tmp2 ;
	PushParam b ;
	LCall _PrintInt ;
	PopParams 4 ;
	PushParam a ;
	LCall _PrintInt ;
	PopParams 4 ;
	PushParam b ;
	LCall _PrintInt ;
	PopParams 4 ;
	EndFunc ;
