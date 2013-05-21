#!/usr/bin/perl
use Text::CSV_XS;


open(OUTPUT, ">dc01-null20.csv") or die $!;

#print the title line
print OUTPUT "Date, Status,DiskP, PagefileP, ProcessN, LoadP, PhyMemP, Conn";
print OUTPUT "\n";

my $file = "data/bbexport-wiz2.csv" or die "Need to get CSV file on the command line\n";
my @rows;
my $csv = Text::CSV_XS->new ({ binary => 1 }) or
     die "Cannot use CSV: ".Text::CSV_XS->error_diag ();
     
open my $fh, "<:encoding(utf8)", $file or die "Could not open '$file' $!\n";

while (my $row = $csv->getline ($fh)) {
 
     
     $ip = $row->[6];
  #	print "ip-".$ip."\n";
		
		if($ip eq "172.10.0.2")
		{
			
			$status = $row->[4];
			$disk = $row->[7];
			$pagefile = $row->[8];
			$numProc = $row->[9];
			$load = $row->[10];
			$phyMem = $row->[11];
			$conn = $row->[12];
			$date = $row->[13];
			
			if($status eq ""){
				$status = "0";
			}
			if($disk eq ""){
				$disk = "0";
			}
			if($pagefile eq ""){
				$pagefile = "0";
			}
			if($numProc eq ""){
				$numProc = "0";
			}
			if($load eq ""){
				$load = "0";
			}
			if($phyMem eq ""){
				$phyMem = "0";
			}
			if($conn eq ""){
				$conn = "0";
			}
		
			#"Date, Status,DiskP, PagefileP, ProcessN, LoadP, PhyMemP, Conn";
			print OUTPUT $date.",".$status.",".$disk.",".$pagefile.",".$numProc.",".$load.",".$phyMem,",".$conn;
			print OUTPUT "\n";
		}
 }
     

$csv->eof or $csv->error_diag ();
 close $fh;


close (OUTPUT);
