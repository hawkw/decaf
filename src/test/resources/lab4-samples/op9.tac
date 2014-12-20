main:
	BeginFunc 20 ;
	_tmp0 = 10 ;
	b = _tmp0 ;
	a = b ;
	PushParam a ;
	LCall _PrintInt ;
	PopParams 4 ;
	PushParam b ;
	LCall _PrintInt ;
	PopParams 4 ;
	EndFunc ;
